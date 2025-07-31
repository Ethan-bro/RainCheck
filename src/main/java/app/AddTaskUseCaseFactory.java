package app;

import data_access.WeatherApiService;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskController;
import interface_adapter.addTask.AddTaskPresenter;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.addTask.UUIDGenerator;
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
            TaskDataAccessInterface taskDao,
            CustomTagDataAccessInterface tagDao,
            WeatherApiService weatherApiService,
            String currentUser,
            String mainViewKey) {

        AddTaskPresenter addTaskPresenter = new AddTaskPresenter(addTaskViewModel, viewManagerModel, mainViewKey);

        AddTaskInputBoundary addTaskInteractor = new AddTaskInteractor(taskDao, new UUIDGenerator(), addTaskPresenter,
                weatherApiService);

        AddTaskController addTaskController = new AddTaskController(currentUser, addTaskInteractor,
                tagDao, viewManagerModel);

        return new AddTaskView(addTaskController, addTaskViewModel, viewManagerModel);
    }
}
