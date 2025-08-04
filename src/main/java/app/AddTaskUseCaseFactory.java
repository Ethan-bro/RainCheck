package app;

import data_access.WeatherApiService;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskController;
import interface_adapter.addTask.AddTaskPresenter;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.addTask.UUIDGenerator;
import interface_adapter.create_customTag.CCTController;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.addTask.AddTaskInputBoundary;
import use_case.addTask.AddTaskInteractor;
import data_access.SupabaseTaskDataAccessObject;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.listTasks.TaskDataAccessInterface;
import view.AddTaskView;

public final class AddTaskUseCaseFactory {
    private AddTaskUseCaseFactory() {}

    public static AddTaskView create(
            ViewManagerModel viewManagerModel,
            AddTaskViewModel addTaskViewModel,
            LoggedInViewModel loggedInViewModel,
            TaskDataAccessInterface taskDao,
            CustomTagDataAccessInterface tagDao,
            WeatherApiService weatherApiService,
            CCTController cctController,
            String mainViewKey) {

        AddTaskPresenter addTaskPresenter = new AddTaskPresenter(addTaskViewModel, viewManagerModel, mainViewKey);

        AddTaskInputBoundary addTaskInteractor = new AddTaskInteractor(taskDao, new UUIDGenerator(), addTaskPresenter,
                weatherApiService);

        AddTaskController addTaskController = new AddTaskController(addTaskInteractor,
                viewManagerModel, addTaskViewModel);

        return new AddTaskView(
                addTaskController,
                addTaskViewModel,
                loggedInViewModel,
                tagDao,
                cctController,
                viewManagerModel);
    }
}
