package app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import data_access.SupabaseTaskDataAccessObject;
import data_access.SupabaseTagDataAccessObject;
import data_access.SupabaseUserDataAccessObject;

import interface_adapter.ManageTags.ManageTagsViewModel;

import data_access.WeatherApiService;
import data_access.NotificationScheduler;
import data_access.EmailNotificationService;
import data_access.FileNotificationDataAccess;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskViewModel;

import interface_adapter.create_customTag.CCTViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.signup.SignupViewModel;
import org.jetbrains.annotations.NotNull;
import use_case.notification.ScheduleNotificationInteractor;
import use_case.notification.ScheduleNotificationOutputBoundary;
import use_case.notification.NotificationDataAccessInterface;
import use_case.notification.EmailNotificationServiceInterface;
import interface_adapter.editTask.EditTaskViewModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.markTaskComplete.MarkTaskCompleteViewModel;
import interface_adapter.deleteTask.DeleteTaskViewModel;
import interface_adapter.logged_in.LoggedInDependencies;
import interface_adapter.task.TaskBoxDependencies;

import view.*;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.CardLayout;
import java.io.FileReader;
import java.io.IOException;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    private final Map<String, JPanel> viewMap = new HashMap<>();

    // Weather:
    private final WeatherApiService weatherApiService = new WeatherApiService();

    // databases:
    private SupabaseUserDataAccessObject userDao;
    private SupabaseTagDataAccessObject tagDao;
    private SupabaseTaskDataAccessObject taskDao;
    private NotificationDataAccessInterface notificationDataAccess;

    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private SignupViewModel signupViewModel;
    private CCTViewModel cctViewModel;
    private ManageTagsViewModel manageTagsViewModel;
    private AddTaskViewModel addTaskViewModel;
    private ListTasksUseCaseFactory listTasksFactory;
    private EmailNotificationServiceInterface emailService;  // Add this field
    private NotificationScheduler notificationScheduler;


    private LoginView loginView;
    private SignupView signupView;
    private LoggedInView loggedInView;
    private AddTaskView addTaskView;


    private EditTaskViewModel editTaskViewModel;
    private MarkTaskCompleteViewModel markTaskCompleteViewModel;
    private DeleteTaskViewModel deleteTaskViewModel;
    private EditTaskController editTaskController;


    public AppBuilder() throws IOException{
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addDatabase() throws Exception {
        JsonObject config = JsonParser.parseReader(new FileReader("config/secrets.json")).getAsJsonObject();
        String dbUrl = config.get("database_url").getAsString();
        String dbAnonKey = config.get("database_anon_key").getAsString();

        userDao = new SupabaseUserDataAccessObject(dbUrl, dbAnonKey);
        tagDao = new SupabaseTagDataAccessObject(dbUrl, dbAnonKey);
        taskDao = new SupabaseTaskDataAccessObject(dbUrl, dbAnonKey);


        // Store notification files in a data directory
        notificationDataAccess = new FileNotificationDataAccess("data/email_configs.json", "data/scheduled_notifications.json");

        // Set up default email configuration for the current user
        setupDefaultEmailConfig(config);

        // Check if email configuration exists in secrets.json
        if (config.has("email_username") && config.has("email_password")) {
            try {
                emailService = new EmailNotificationService(
                        "smtp.gmail.com", // Gmail SMTP server
                        "587",  // TLS port for Gmail
                        config.get("email_username").getAsString(),
                        config.get("email_password").getAsString()
                );

                System.out.println("Email service initialized successfully");

            } catch (Exception e) {
                System.err.println("Email service configuration error: " + e.getMessage());
                e.printStackTrace();
                // Fall back to dummy service
                emailService = createDummyEmailService();
            }
        } else {
            System.out.println("Warning: Email configuration missing. Email notifications will not work.");
            emailService = createDummyEmailService();
        }

        // Initialize and start notification scheduler
        notificationScheduler = new NotificationScheduler(notificationDataAccess, emailService, taskDao);
        notificationScheduler.start();

        return this;
            }

            private EmailNotificationService createDummyEmailService() {
        return new EmailNotificationService(
                "smtp.example.com",
                "587",
                "dummy@example.com",
                "dummy_password"
        );
    }

    private void setupDefaultEmailConfig(JsonObject config) {
        try {
            // Check if current user exists
            String currentUsername = userDao.getCurrentUsername();
            if (currentUsername == null || currentUsername.isEmpty()) {
                System.out.println("No user logged in, skipping email config setup");
                return;
            }

            // Check if email config exists for current user
            entity.EmailNotificationConfig emailConfig = notificationDataAccess.getEmailConfig(currentUsername);

            if (emailConfig == null && config.has("email_username")) {
                // Create email config for the current user
                String email = config.get("email_username").getAsString();
                entity.EmailNotificationConfig newConfig = new entity.EmailNotificationConfig(email, true);
                notificationDataAccess.saveEmailConfig(currentUsername, newConfig);
                System.out.println("Created email notification config for user: " + currentUsername);
            }
        } catch (Exception e) {
            System.err.println("Could not set up email config: " + e.getMessage());
        }
    }

    public AppBuilder addViewModels() {
        loginViewModel = new LoginViewModel();
        loggedInViewModel = new LoggedInViewModel();
        signupViewModel = new SignupViewModel();
        cctViewModel = new CCTViewModel();
        manageTagsViewModel = new ManageTagsViewModel(tagDao, userDao.getCurrentUsername());
        addTaskViewModel = new AddTaskViewModel(tagDao, userDao.getCurrentUsername());
        editTaskViewModel = new EditTaskViewModel(tagDao, userDao.getCurrentUsername());
        markTaskCompleteViewModel = new MarkTaskCompleteViewModel();
        deleteTaskViewModel = new DeleteTaskViewModel();

        return this;
    }

    public AppBuilder addSignupView() {
        SignupView signupView = SignupUseCaseFactory.create(viewManagerModel, loginViewModel, signupViewModel, userDao);
        cardPanel.add(signupView, SignupView.getViewName());
        viewMap.put(SignupView.getViewName(), signupView);
        return this;
    }

    public AppBuilder addLoginView() {
        LoginView loginView = LoginUseCaseFactory.create(viewManagerModel, loginViewModel, loggedInViewModel, signupViewModel, userDao);
        cardPanel.add(loginView, loginView.getViewName());
        viewMap.put(loginView.getViewName(), loginView);
        return this;
    }

    public AppBuilder addListTasksUseCase() {
        ListTasksUseCaseFactory listTasksFactory = new ListTasksUseCaseFactory(taskDao, loggedInViewModel);
        listTasksFactory.create();
        return this;
    }

    public AppBuilder addLoggedInView() throws IOException {

        LogoutController logoutController = LogoutUseCaseFactory.create(
                viewManagerModel, loggedInViewModel, loginViewModel, userDao);

        LoggedInDependencies loggedInDependencies = new LoggedInDependencies(
                loggedInViewModel,
                logoutController
        );

        TaskBoxDependencies taskBoxDependencies = new TaskBoxDependencies(
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


    public AppBuilder addAddTaskView() {
        
        try {
            // Create a simple output boundary for notifications (you may need to implement this properly)
            ScheduleNotificationInteractor notificationInteractor = getScheduleNotificationInteractor();

            addTaskView = AddTaskUseCaseFactory.create(
                    viewManagerModel,
                    addTaskViewModel,
                    loggedInViewModel,
                    taskDao,
                    new WeatherApiService(),
                    notificationInteractor,
                    LoggedInView.getViewName()
            );
        } catch (IOException e) {
            System.err.println("Weather Lookup Failed: " + e.getMessage());
        }

        cardPanel.add(addTaskView, AddTaskView.getViewName());
        viewMap.put(AddTaskView.getViewName(), addTaskView);
        return this;
    }

    @NotNull
    private ScheduleNotificationInteractor getScheduleNotificationInteractor() {
        ScheduleNotificationOutputBoundary notificationOutputBoundary = outputData -> {
            // Simple implementation - you can enhance this later
            if (outputData.isSuccess()) {
                System.out.println("Notification scheduled successfully: " + outputData.getNotificationId());
            } else {
                System.err.println("Failed to schedule notification: " + outputData.getMessage());
            }
        };


        // Create the notification interactor
        return new ScheduleNotificationInteractor(
                notificationDataAccess,
                taskDao,  // taskDao should implement EditTaskDataAccessInterface
                notificationOutputBoundary
        );
    }


    public AppBuilder addCCTView() {
        if (this.cctViewModel == null) return this; // do nothing

        CCTView cctView = CCTUseCaseFactory.create(
                this.viewManagerModel,
                this.cctViewModel,
                this.manageTagsViewModel,
                this.tagDao
        );
        cardPanel.add(cctView, CCTView.getViewName());
        viewMap.put(CCTView.getViewName(), cctView);
        return this;
    }

    public AppBuilder addManageTagsView() {
        if (this.manageTagsViewModel == null) return this;

        ManageTagsView manageTagsView = ManageTagsUseCaseFactory.create(
                viewManagerModel,
                manageTagsViewModel,
                cctViewModel,
                tagDao
        );

        // 3) Register it in the CardLayout
        cardPanel.add(manageTagsView, ManageTagsView.getViewName());
        viewMap.put(ManageTagsView.getViewName(), manageTagsView);

        return this;
    }


    public AppBuilder addEditTaskView() {
        editTaskController = EditTaskUseCaseFactory.createController(taskDao, editTaskViewModel, viewManagerModel, weatherApiService);
        EditTaskView editTaskView = EditTaskUseCaseFactory.createView(
                editTaskController, editTaskViewModel, viewManagerModel, LoggedInView.getViewName()
        );
        cardPanel.add(editTaskView, EditTaskView.getViewName());
        viewMap.put(EditTaskView.getViewName(), editTaskView);
        return this;
    }

    public AppBuilder addTaskViews() {
        // Add TaskViews implementation here if needed  
        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("RainCheck");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        // Initialize ViewManagerModel with view map
        viewManagerModel.setViewMap(viewMap);

        // Add shutdown hook to stop scheduler when app closes
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