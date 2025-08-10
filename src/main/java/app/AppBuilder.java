package app;

import data_access.EmailNotificationService;
import data_access.FileNotificationDataAccess;
import data_access.NotificationScheduler;
import data_access.SupabaseTagDataAccessObject;
import data_access.SupabaseTaskDataAccessObject;
import data_access.SupabaseUserDataAccessObject;
import data_access.WeatherApiService;

import entity.EmailNotificationConfig;

import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.createTag.CreateCustomTagViewModel;
import interface_adapter.deleteTask.DeleteTaskViewModel;
import interface_adapter.editTag.EditTagViewModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskViewModel;
import interface_adapter.logged_in.LoggedInDependencies;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.manageTags.ManageTagsViewModel;
import interface_adapter.markTaskComplete.MarkTaskCompleteViewModel;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.task.TaskBoxDependencies;

import use_case.notification.EmailNotificationServiceInterface;
import use_case.notification.NotificationDataAccessInterface;
import use_case.notification.ScheduleNotificationInteractor;
import use_case.notification.ScheduleNotificationOutputBoundary;

import view.AddTaskView;
import view.CreateCustomTagView;
import view.EditTagView;
import view.EditTaskView;
import view.GmailInstructionsView;
import view.LoggedInView;
import view.LoginView;
import view.ManageTagsView;
import view.SignupView;
import view.ViewManager;

import java.awt.CardLayout;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Builder class for the application views and dependencies.
 */
public final class AppBuilder {
    private static final String CONFIG_PATH = "config/secrets.json";
    private static final String EMAIL_CONFIG_PATH = "data/email_configs.json";
    private static final String NOTIFICATION_SCHEDULE_PATH = "data/scheduled_notifications.json";
    private static final String EMAIL_USERNAME_KEY = "email_username";
    private static final String EMAIL_PASSWORD_KEY = "email_password";
    private static final String DATABASE_URL_KEY = "database_url";
    private static final String DATABASE_ANON_KEY = "database_anon_key";

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    private final Map<String, JPanel> viewMap = new HashMap<>();

    private final WeatherApiService weatherApiService = new WeatherApiService();

    private SupabaseUserDataAccessObject userDao;
    private SupabaseTagDataAccessObject tagDao;
    private SupabaseTaskDataAccessObject taskDao;
    private NotificationDataAccessInterface notificationDataAccess;

    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private SignupViewModel signupViewModel;
    private CreateCustomTagViewModel cctViewModel;
    private EditTagViewModel editTagViewModel;
    private ManageTagsViewModel manageTagsViewModel;
    private AddTaskViewModel addTaskViewModel;
    private EditTaskViewModel editTaskViewModel;
    private MarkTaskCompleteViewModel markTaskCompleteViewModel;
    private DeleteTaskViewModel deleteTaskViewModel;

    private EmailNotificationServiceInterface emailService;
    private NotificationScheduler notificationScheduler;

    private LoggedInView loggedInView;
    private AddTaskView addTaskView;
    private EditTaskController editTaskController;

    public AppBuilder() throws IOException {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Adds database connections and configures email notification services.
     *
     * @return this AppBuilder instance
     * @throws IOException if the config file cannot be read
     */
    public AppBuilder addDatabase() throws IOException {
        final JsonObject config;
        try (FileReader fileReader = new FileReader(CONFIG_PATH)) {
            config = JsonParser.parseReader(fileReader).getAsJsonObject();
        }
        catch (FileNotFoundException ex) {
            throw new IOException("Config file not found: " + CONFIG_PATH, ex);
        }

        final String dbUrl = config.get(DATABASE_URL_KEY).getAsString();
        final String dbAnonKey = config.get(DATABASE_ANON_KEY).getAsString();

        userDao = new SupabaseUserDataAccessObject(dbUrl, dbAnonKey);
        tagDao = new SupabaseTagDataAccessObject(dbUrl, dbAnonKey);
        taskDao = new SupabaseTaskDataAccessObject(dbUrl, dbAnonKey);

        notificationDataAccess = new FileNotificationDataAccess(EMAIL_CONFIG_PATH, NOTIFICATION_SCHEDULE_PATH);

        setupDefaultEmailConfig(config);

        if (config.has(EMAIL_USERNAME_KEY) && config.has(EMAIL_PASSWORD_KEY)) {
            initializeEmailService(config);
        }
        else {
            System.out.println("Warning: Email configuration missing. Email notifications will not work.");
            emailService = createDummyEmailService();
        }

        notificationScheduler = new NotificationScheduler(notificationDataAccess, emailService, taskDao);
        notificationScheduler.start();

        return this;
    }

    private void initializeEmailService(final JsonObject config) {
        emailService = new EmailNotificationService(
                "smtp.gmail.com",
                "587",
                config.get(EMAIL_USERNAME_KEY).getAsString(),
                config.get(EMAIL_PASSWORD_KEY).getAsString()
        );
        System.out.println("Email service initialized successfully");
    }

    private EmailNotificationService createDummyEmailService() {
        return new EmailNotificationService(
                "smtp.example.com",
                "587",
                "dummy@example.com",
                "dummy_password"
        );
    }

    private void setupDefaultEmailConfig(final JsonObject config) {
        final String currentUsername = userDao.getCurrentUsername();
        if (currentUsername != null && !currentUsername.isEmpty()) {
            final EmailNotificationConfig emailConfig = notificationDataAccess.getEmailConfig(currentUsername);
            if (emailConfig == null && config.has(EMAIL_USERNAME_KEY)) {
                final String email = config.get(EMAIL_USERNAME_KEY).getAsString();
                final EmailNotificationConfig newConfig = new EmailNotificationConfig(email, true);
                notificationDataAccess.saveEmailConfig(currentUsername, newConfig);
                System.out.println("Created email notification config for user: " + currentUsername);
            }
        }
        else {
            System.out.println("No user logged in, skipping email config setup");
        }
    }

    /**
     * Initializes all view models.
     *
     * @return this AppBuilder instance
     */
    public AppBuilder addViewModels() {
        loginViewModel = new LoginViewModel();
        loggedInViewModel = new LoggedInViewModel();
        signupViewModel = new SignupViewModel();
        cctViewModel = new CreateCustomTagViewModel();
        editTagViewModel = new EditTagViewModel();
        manageTagsViewModel = new ManageTagsViewModel(tagDao, userDao.getCurrentUsername());
        addTaskViewModel = new AddTaskViewModel(tagDao, userDao.getCurrentUsername());
        editTaskViewModel = new EditTaskViewModel(tagDao, userDao.getCurrentUsername());
        markTaskCompleteViewModel = new MarkTaskCompleteViewModel();
        deleteTaskViewModel = new DeleteTaskViewModel();
        return this;
    }

    /**
     * Adds signup view to the card panel.
     *
     * @return this AppBuilder instance
     */
    public AppBuilder addSignupView() {
        final SignupView localSignupView = SignupUseCaseFactory.create(
                viewManagerModel,
                loginViewModel,
                signupViewModel,
                userDao
        );
        cardPanel.add(localSignupView, SignupView.getViewName());
        viewMap.put(SignupView.getViewName(), localSignupView);
        return this;
    }

    /**
     * Adds login view to the card panel.
     *
     * @return this AppBuilder instance
     */
    public AppBuilder addLoginView() {
        final LoginView localLoginView = LoginUseCaseFactory.create(
                viewManagerModel,
                loginViewModel,
                loggedInViewModel,
                signupViewModel,
                userDao
        );
        cardPanel.add(localLoginView, localLoginView.getViewName());
        viewMap.put(localLoginView.getViewName(), localLoginView);
        return this;
    }

    /**
     * Adds list tasks use case.
     *
     * @return this AppBuilder instance
     */
    public AppBuilder addListTasksUseCase() {
        final ListTasksUseCaseFactory listTasksFactory = new ListTasksUseCaseFactory(taskDao, loggedInViewModel);
        listTasksFactory.create();
        return this;
    }

    /**
     * Adds logged in view to the card panel.
     *
     * @return this AppBuilder instance
     * @throws IOException if view creation fails
     */
    public AppBuilder addLoggedInView() throws IOException {
        final LogoutController logoutController = LogoutUseCaseFactory.create(
                viewManagerModel,
                loggedInViewModel,
                loginViewModel,
                userDao
        );

        final LoggedInDependencies loggedInDependencies = new LoggedInDependencies(
                loggedInViewModel,
                logoutController
        );

        final TaskBoxDependencies taskBoxDependencies = new TaskBoxDependencies(
                markTaskCompleteViewModel,
                deleteTaskViewModel,
                viewManagerModel,
                editTaskController,
                editTaskViewModel
        );

        loggedInView = LoggedInUseCaseFactory.createLoggedInView(
                loggedInDependencies,
                addTaskViewModel,
                manageTagsViewModel,
                taskDao,
                taskBoxDependencies,
                notificationDataAccess
        );

        cardPanel.add(loggedInView, LoggedInView.getViewName());
        return this;
    }

    /**
     * Adds the Gmail Instructions view to the application's card panel and view map.
     * This view provides users with setup instructions for Gmail integration,
     * including a "Go Back" button to return to the previous view.
     *
     * @return the current AppBuilder instance for method chaining
     */
    public AppBuilder addGmailInstructionsView() {
        final GmailInstructionsView gmailInstructionsView = new GmailInstructionsView(viewManagerModel);

        cardPanel.add(gmailInstructionsView, GmailInstructionsView.getViewName());
        viewMap.put(GmailInstructionsView.getViewName(), gmailInstructionsView);

        return this;
    }

    /**
     * Adds add task view to the card panel.
     *
     * @return this AppBuilder instance
     */
    public AppBuilder addAddTaskView() {
        final ScheduleNotificationInteractor notificationInteractor = getScheduleNotificationInteractor();

        addTaskView = AddTaskUseCaseFactory.create(
                viewManagerModel,
                addTaskViewModel,
                loggedInViewModel,
                taskDao,
                weatherApiService,
                notificationInteractor,
                LoggedInView.getViewName()
        );

        cardPanel.add(addTaskView, AddTaskView.getViewName());
        viewMap.put(AddTaskView.getViewName(), addTaskView);
        return this;
    }

    private ScheduleNotificationInteractor getScheduleNotificationInteractor() {
        final ScheduleNotificationOutputBoundary notificationOutputBoundary = outputData -> {
            if (outputData.success()) {
                System.out.println("Notification scheduled successfully: " + outputData.notificationId());
            }
            else {
                System.err.println("Failed to schedule notification: " + outputData.message());
            }
        };
        return new ScheduleNotificationInteractor(notificationDataAccess, taskDao, notificationOutputBoundary);
    }

    /**
     * Adds custom tag creation view.
     * @return this AppBuilder instance
     */
    public AppBuilder addCctView() {
        if (cctViewModel != null) {
            final CreateCustomTagView cctView = CctUseCaseFactory.create(
                    viewManagerModel,
                    cctViewModel,
                    manageTagsViewModel,
                    tagDao
            );
            cardPanel.add(cctView, CreateCustomTagView.getViewName());
            viewMap.put(CreateCustomTagView.getViewName(), cctView);
        }
        return this;
    }

    /**
     * Adds edit tag view.
     *
     * @return this AppBuilder instance
     */
    public AppBuilder addEditTagView() {
        if (editTagViewModel != null) {
            final EditTagView editTagView = EditTagUseCaseFactory.create(
                    viewManagerModel,
                    editTagViewModel,
                    manageTagsViewModel,
                    tagDao
            );
            cardPanel.add(editTagView, EditTagView.getViewName());
            viewMap.put(EditTagView.getViewName(), editTagView);
        }
        return this;
    }

    /**
     * Adds manage tags view.
     *
     * @return this AppBuilder instance
     */
    public AppBuilder addManageTagsView() {
        if (manageTagsViewModel != null) {
            final ManageTagsView manageTagsView = ManageTagsUseCaseFactory.create(
                    viewManagerModel,
                    manageTagsViewModel,
                    cctViewModel,
                    editTagViewModel,
                    tagDao
            );
            cardPanel.add(manageTagsView, ManageTagsView.getViewName());
            viewMap.put(ManageTagsView.getViewName(), manageTagsView);
        }
        return this;
    }

    /**
     * Adds edit task view.
     *
     * @return this AppBuilder instance
     */
    public AppBuilder addEditTaskView() {
        editTaskController = EditTaskUseCaseFactory.createController(
                taskDao,
                editTaskViewModel,
                viewManagerModel,
                weatherApiService
        );

        final EditTaskView editTaskView = EditTaskUseCaseFactory.createView(
                editTaskController,
                editTaskViewModel,
                viewManagerModel,
                LoggedInView.getViewName()
        );
        cardPanel.add(editTaskView, EditTaskView.getViewName());
        viewMap.put(EditTaskView.getViewName(), editTaskView);
        return this;
    }

    /**
     * Build and return the main application JFrame.
     *
     * @return the main application JFrame
     */
    public JFrame build() {
        final JFrame application = new JFrame("RainCheck");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManagerModel.setViewMap(viewMap);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (notificationScheduler != null) {
                notificationScheduler.stop();
            }
        }));

        viewManagerModel.setState(SignupView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
