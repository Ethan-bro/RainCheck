package app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import data_access.SupabaseTaskDataAccessObject;
import data_access.SupabaseTagDataAccessObject;
import data_access.SupabaseUserDataAccessObject;

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
import use_case.notification.ScheduleNotificationInteractor;
import use_case.notification.ScheduleNotificationOutputBoundary;
import use_case.notification.NotificationDataAccessInterface;
import use_case.notification.EmailNotificationServiceInterface;



import view.*;

import javax.swing.*;
import java.awt.CardLayout;
import java.io.FileReader;
import java.io.IOException;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // databases:
    private SupabaseUserDataAccessObject userDao;
    private SupabaseTagDataAccessObject tagDao;
    private SupabaseTaskDataAccessObject taskDao;
    private NotificationDataAccessInterface notificationDataAccess;

    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private SignupViewModel signupViewModel;
    private AddTaskViewModel addTaskViewModel;
    private ListTasksUseCaseFactory listTasksFactory;
    private EmailNotificationServiceInterface emailService;  // Add this field
    private NotificationScheduler notificationScheduler;


    private LoginView loginView;
    private SignupView signupView;
    private LoggedInView loggedInView;
    private AddTaskView addTaskView;

    public AppBuilder() {
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

        // Check if email configuration exists in secrets.json
        if (config.has("email_username") && config.has("email_password")) {
            emailService = new EmailNotificationService(
                    "smtp.gmail.com", // or your SMTP server
                    "587",
                    config.get("email_username").getAsString(),
                    config.get("email_password").getAsString()
            );
        } else {
            System.out.println("Warning: Email configuration missing. Email notifications will not work.");
            // Provide a dummy email service to avoid null pointer exceptions
            emailService = new EmailNotificationService(
                    "smtp.example.com",
                    "587",
                    "dummy@example.com",
                    "dummy_password"
            );
        }

        // Initialize and start notification scheduler
        notificationScheduler = new NotificationScheduler(notificationDataAccess, emailService, taskDao);
        notificationScheduler.start();

        return this;
    }

    public AppBuilder addViewModels() {
        loginViewModel = new LoginViewModel();
        loggedInViewModel = new LoggedInViewModel();
        signupViewModel = new SignupViewModel();
        addTaskViewModel = new AddTaskViewModel(tagDao, userDao.getCurrentUsername());
        return this;
    }

    public AppBuilder addSignupView() {
        signupView = SignupUseCaseFactory.create(viewManagerModel, loginViewModel, signupViewModel, userDao);
        cardPanel.add(signupView, SignupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginView = LoginUseCaseFactory.create(viewManagerModel, loginViewModel, loggedInViewModel, signupViewModel, userDao);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addListTasksUseCase() {
        listTasksFactory = new ListTasksUseCaseFactory(taskDao, loggedInViewModel);
        listTasksFactory.create();
        return this;
    }

    public AppBuilder addLoggedInView() throws IOException {

        LogoutController logoutController = LogoutUseCaseFactory.create(
                viewManagerModel, loggedInViewModel, loginViewModel, userDao);

        loggedInView = LoggedInUseCaseFactory.createLoggedInView(loggedInViewModel, logoutController, viewManagerModel, addTaskViewModel, tagDao, taskDao);

        cardPanel.add(loggedInView, LoggedInView.getViewName());
        return this;
    }

    public AppBuilder addAddTaskView() {

        try {
            // Create a simple output boundary for notifications (you may need to implement this properly)
            ScheduleNotificationOutputBoundary notificationOutputBoundary = new ScheduleNotificationOutputBoundary() {
                @Override
                public void presentScheduleResult(use_case.notification.ScheduleNotificationOutputData outputData) {
                    // Simple implementation - you can enhance this later
                    if (outputData.isSuccess()) {
                        System.out.println("Notification scheduled successfully: " + outputData.getNotificationId());
                    } else {
                        System.err.println("Failed to schedule notification: " + outputData.getMessage());
                    }
                }
            };

            // Create the notification interactor
            ScheduleNotificationInteractor notificationInteractor = new ScheduleNotificationInteractor(
                    notificationDataAccess,
                    taskDao,  // taskDao should implement EditTaskDataAccessInterface
                    notificationOutputBoundary
            );

            addTaskView = AddTaskUseCaseFactory.create(
                    viewManagerModel,
                    addTaskViewModel,
                    loggedInViewModel,
                    taskDao,
                    tagDao,
                    new WeatherApiService(),
                    notificationInteractor,
                    LoggedInView.getViewName()
            );
        } catch (IOException e) {
            System.err.println("Weather Lookup Failed: " + e.getMessage());
        }

        cardPanel.add(addTaskView, AddTaskView.getViewName());
        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("RainCheck");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

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
