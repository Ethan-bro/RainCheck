package app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import data_access.*;
import interface_adapter.*;
import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.create_customTag.CCTViewModel;
import interface_adapter.deleteTask.DeleteTaskViewModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskViewModel;
import interface_adapter.logged_in.LoggedInDependencies;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.markTaskComplete.MarkTaskCompleteViewModel;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.task.TaskBoxDependencies;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    private final Map<String, JPanel> viewMap = new HashMap<>();

    // Weather:
    private final WeatherApiService weatherApiService = new WeatherApiService();

    // DAOs
    private SupabaseUserDataAccessObject userDao;
    private SupabaseTagDataAccessObject tagDao;
    private SupabaseTaskDataAccessObject taskDao;

    // ViewModels
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private SignupViewModel signupViewModel;
    private CCTViewModel cctViewModel;
    private ManageTagsViewModel manageTagsViewModel;
    private AddTaskViewModel addTaskViewModel;
    private EditTaskViewModel editTaskViewModel;

    // Controllers
    private EditTaskController editTaskController;

    public AppBuilder() throws IOException {
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
        cctViewModel = new CCTViewModel();
        manageTagsViewModel = new ManageTagsViewModel(tagDao, userDao.getCurrentUsername());
        addTaskViewModel = new AddTaskViewModel(tagDao, userDao.getCurrentUsername());
        editTaskViewModel = new EditTaskViewModel(tagDao, userDao.getCurrentUsername());
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

    public AppBuilder addCCTView() {
        if (this.cctViewModel == null) return this; // do nothing

        CCTView cctView = CCTUseCaseFactory.create(
                this.viewManagerModel,
                this.cctViewModel,
                this.tagDao
        );
        cardPanel.add(cctView, CCTView.getViewName());
        viewMap.put(CCTView.getViewName(), cctView);
        return this;
    }

    public AppBuilder addManageTagsView() {
        if (this.manageTagsViewModel == null) return this;

        ManageTagsView manageTagsView = ManageTagsUseCaseFactory.create(
                loggedInViewModel,
                viewManagerModel,
                manageTagsViewModel,
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

    public AppBuilder addLoggedInView() throws IOException {
        LogoutController logoutController = LogoutUseCaseFactory.create(
                viewManagerModel, loggedInViewModel, loginViewModel, userDao
        );

        LoggedInDependencies loggedInDependencies = new LoggedInDependencies(loggedInViewModel, logoutController);

        TaskBoxDependencies taskBoxDependencies = new TaskBoxDependencies(
                new MarkTaskCompleteViewModel(),
                new DeleteTaskViewModel(),
                viewManagerModel,
                editTaskController,
                editTaskViewModel
        );

        LoggedInView loggedInView = LoggedInUseCaseFactory.createLoggedInView(
                loggedInDependencies,
                addTaskViewModel,
                manageTagsViewModel,
                tagDao,
                taskDao,
                taskBoxDependencies
        );

        cardPanel.add(loggedInView, LoggedInView.getViewName());
        viewMap.put(LoggedInView.getViewName(), loggedInView);
        return this;
    }

    public AppBuilder addTaskViews() {
        AddTaskView addTaskView = AddTaskUseCaseFactory.create(
                viewManagerModel,
                addTaskViewModel,
                loggedInViewModel,
                taskDao,
                weatherApiService,
                LoggedInView.getViewName()
        );
        cardPanel.add(addTaskView, AddTaskView.getViewName());
        viewMap.put(AddTaskView.getViewName(), addTaskView);
        return this;
    }

    public AppBuilder addListTasksUseCase() {
        ListTasksUseCaseFactory listTasksFactory = new ListTasksUseCaseFactory(taskDao, loggedInViewModel);
        listTasksFactory.create();
        return this;
    }

    public JFrame build() {
        viewManagerModel.setViewMap(viewMap);

        JFrame application = new JFrame("RainCheck");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManagerModel.setState(SignupView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
