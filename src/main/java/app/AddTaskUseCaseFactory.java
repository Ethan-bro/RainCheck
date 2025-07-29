package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskController;
import interface_adapter.addTask.AddTaskPresenter;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.addTask.UUIDGenerator;
import use_case.addTask.AddTaskInputBoundary;
import use_case.addTask.AddTaskInteractor;
import data_access.SupabaseTaskDataAccessObject;
import view.AddTaskView;

public final class AddTaskUseCaseFactory {
    private AddTaskUseCaseFactory() {}

    public static AddTaskView create(
            ViewManagerModel viewManagerModel,
            AddTaskViewModel addTaskViewModel,
            SupabaseTaskDataAccessObject taskDao,
            String currentUser,
            String mainViewKey) {

        AddTaskPresenter addTaskPresenter = new AddTaskPresenter(addTaskViewModel, viewManagerModel, mainViewKey);

        AddTaskInputBoundary addTaskInteractor = new AddTaskInteractor(taskDao, new UUIDGenerator(), addTaskPresenter);

        AddTaskController addTaskController = new AddTaskController(currentUser, addTaskInteractor);

        return new AddTaskView(addTaskController, addTaskViewModel, viewManagerModel);
    }
}
