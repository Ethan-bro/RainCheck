package app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import data_access.SupabaseTaskDataAccessObject;
import data_access.SupabaseTagDataAccessObject;
import data_access.SupabaseUserDataAccessObject;

import data_access.WeatherApiService;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskViewModel;

import interface_adapter.create_customTag.CCTViewModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskPresenter;
import interface_adapter.editTask.EditTaskViewModel;
import interface_adapter.logged_in.LoggedInDependencies;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.signup.SignupViewModel;

import org.jetbrains.annotations.NotNull;
import use_case.editTask.EditTaskInteractor;
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
    private ListTasksUseCaseFactory listTasksFactory;

    private EditTaskViewModel editTaskViewModel;
    private EditTaskPresenter editTaskPresenter;
    private EditTaskInteractor editTaskInteractor;
    private EditTaskController editTaskController;
    private EditTaskView editTaskView;

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

        LoggedInDependencies loggedInDependencies = new LoggedInDependencies(loggedInViewModel, logoutController);
        loggedInView = LoggedInUseCaseFactory.createLoggedInView(loggedInDependencies, viewManagerModel, addTaskViewModel, tagDao, taskDao);

        cardPanel.add(loggedInView, LoggedInView.getViewName());
        return this;
    }

    public AppBuilder addTaskViews() {

        try {
            // Add Task View
            addTaskView = AddTaskUseCaseFactory.create(
                    viewManagerModel,
                    addTaskViewModel,
                    loggedInViewModel,
                    taskDao,
                    tagDao,
                    new WeatherApiService(),
                    LoggedInView.getViewName()
            );
            cardPanel.add(addTaskView, AddTaskView.getViewName());

            // Edit Task View
            initEditTaskUseCase();
            cardPanel.add(editTaskView, EditTaskView.getViewName());

        } catch (IOException e) {
            System.err.println("Weather Lookup Failed: " + e.getMessage());
        }

        return this;
    }

    private void initEditTaskUseCase() {
        if (editTaskView != null) return; // ensuring this init is only called once

        editTaskViewModel = new EditTaskViewModel(tagDao);
        editTaskPresenter = new EditTaskPresenter(editTaskViewModel);
        editTaskInteractor = new EditTaskInteractor(taskDao, editTaskPresenter);
        editTaskController = new EditTaskController(editTaskInteractor, viewManagerModel);
        editTaskView = new EditTaskView(editTaskController, editTaskViewModel, viewManagerModel, LoggedInView.getViewName());
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
