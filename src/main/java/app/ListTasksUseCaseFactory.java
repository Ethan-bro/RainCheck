package app;

import interface_adapter.logged_in.ListTasksPresenter;
import interface_adapter.logged_in.LoggedInViewModel;

import use_case.listTasks.ListTasksInputBoundary;
import use_case.listTasks.ListTasksInteractor;
import use_case.listTasks.ListTasksOutputBoundary;
import use_case.listTasks.TaskDataAccessInterface;

public class ListTasksUseCaseFactory {

    private final TaskDataAccessInterface taskDao;
    private final LoggedInViewModel loggedInViewModel;

    public ListTasksUseCaseFactory(final TaskDataAccessInterface taskDao, final LoggedInViewModel loggedInViewModel) {
        this.taskDao = taskDao;
        this.loggedInViewModel = loggedInViewModel;
    }

    /**
     * Creates and returns a ListTasksInputBoundary interactor.
     * Also sets the interactor in the LoggedInViewModel.
     *
     * @return a ListTasksInputBoundary instance for handling list tasks use case
     */
    public ListTasksInputBoundary create() {
        final ListTasksOutputBoundary presenter = new ListTasksPresenter(loggedInViewModel);
        final ListTasksInputBoundary interactor = new ListTasksInteractor(taskDao, presenter);
        loggedInViewModel.setListTasksInteractor(interactor);
        return interactor;
    }
}
