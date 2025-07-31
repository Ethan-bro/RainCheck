package app;

import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.ListTasksPresenter;
import use_case.listTasks.ListTasksInputBoundary;
import use_case.listTasks.ListTasksInteractor;
import use_case.listTasks.ListTasksOutputBoundary;
import use_case.listTasks.TaskDataAccessInterface;

public class ListTasksUseCaseFactory {

    private final TaskDataAccessInterface taskDao;
    private final LoggedInViewModel loggedInViewModel;

    public ListTasksUseCaseFactory(TaskDataAccessInterface taskDao, LoggedInViewModel loggedInViewModel) {
        this.taskDao = taskDao;
        this.loggedInViewModel = loggedInViewModel;
    }

    public ListTasksInputBoundary create() {
        ListTasksOutputBoundary presenter = new ListTasksPresenter(loggedInViewModel);
        ListTasksInputBoundary interactor = new ListTasksInteractor(taskDao, presenter);
        loggedInViewModel.setListTasksInteractor(interactor);
        return interactor;
    }
}
