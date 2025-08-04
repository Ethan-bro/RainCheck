package app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import data_access.SupabaseTaskDataAccessObject;
import data_access.SupabaseTagDataAccessObject;
import data_access.SupabaseUserDataAccessObject;

import data_access.WeatherApiService;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskViewModel;

import interface_adapter.create_customTag.CCTController;
import interface_adapter.create_customTag.CCTViewModel;
import interface_adapter.editTask.EditTaskViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.signup.SignupViewModel;

import interface_adapter.task.TaskViewModel;
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

    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private SignupViewModel signupViewModel;
    private AddTaskViewModel addTaskViewModel;
    private EditTaskViewModel editTaskViewModel;
    private CCTViewModel cctViewModel;
    private TaskViewModel taskViewModel;
    private ListTasksUseCaseFactory listTasksFactory;

    CCTController cctController;

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

        return this;
    }

    public AppBuilder addViewModels() {
        loginViewModel = new LoginViewModel();
        loggedInViewModel = new LoggedInViewModel();
        signupViewModel = new SignupViewModel();
        addTaskViewModel = new AddTaskViewModel(tagDao, userDao.getCurrentUsername());
        editTaskViewModel = new EditTaskViewModel(tagDao);
        cctViewModel = new CCTViewModel();

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

        loggedInView = LoggedInUseCaseFactory.createLoggedInView(
                loggedInViewModel,
                logoutController,
                viewManagerModel,
                viewManager,
                addTaskViewModel,
                editTaskViewModel,
                tagDao,
                taskDao);

        cardPanel.add(loggedInView, LoggedInView.getViewName());
        return this;
    }

    /** 1) Build your CCT use‐case and register its view. */
    public AppBuilder addCCTUseCase() {

        cctController = CCTUseCaseFactory.create(
                viewManagerModel,
                cctViewModel,
                tagDao,
                loggedInViewModel
        );
        // extract the view from the controller and register it
        cardPanel.add(
                cctController.getView(),
                CCTView.getViewName()
        );
        return this;
    }

    public AppBuilder addAddTaskView() {

        try {
            addTaskView = AddTaskUseCaseFactory.create(
                    viewManagerModel,
                    addTaskViewModel,
                    loggedInViewModel,
                    taskDao,
                    tagDao,
                    new WeatherApiService(),
                    cctController,
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

        viewManagerModel.setState(SignupView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
