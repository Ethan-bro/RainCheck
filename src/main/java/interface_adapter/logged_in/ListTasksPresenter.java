package interface_adapter.logged_in;

import entity.Task;
import use_case.listTasks.ListTasksOutputBoundary;

import java.util.List;

/**
 * Presenter class for the ListTasks use case.
 * Responsible for transforming the use case output into a state update
 * in the LoggedInViewModel, and notifying the view via property change.
 */
public class ListTasksPresenter implements ListTasksOutputBoundary {

    private final LoggedInViewModel viewModel;

    /**
     * Constructs a ListTasksPresenter with the associated LoggedInViewModel.
     *
     * @param viewModel The view model to update when tasks are listed.
     */
    public ListTasksPresenter(LoggedInViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Called by the interactor to present the fetched list of tasks.
     * Updates the LoggedInState with the new list of tasks, then fires
     * a property change for the "weekTasks" property to notify the view.
     *
     * @param tasks The List<Task> containing tasks.
     */
    @Override
    public void presentTasks(List<Task> tasks) {
        LoggedInState currentState = viewModel.getState();

        // Defensive check: even if null, set empty list
        if (tasks == null) {
            tasks = List.of(); // or Collections.emptyList()
        }

        currentState.setWeekTasks(tasks);
        viewModel.setState(currentState); // optional if your ViewModel.setState fires internally

        // Notify the view that task list has changed
        viewModel.firePropertyChanged("weekTasks");
    }
}
