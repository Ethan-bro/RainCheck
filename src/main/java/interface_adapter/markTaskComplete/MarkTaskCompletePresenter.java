package interface_adapter.markTaskComplete;

import interface_adapter.task.TaskViewModel;
import use_case.markTaskComplete.MarkTaskCompleteOutputBoundary;
import use_case.markTaskComplete.MarkTaskCompleteOutputData;

public class MarkTaskCompletePresenter implements MarkTaskCompleteOutputBoundary {

    private final MarkTaskCompleteViewModel viewModel;
    private final TaskViewModel taskViewModel;


    /**
     * Constructs a MarkTaskCompletePresenter with the given view models.
     *
     * @param viewModel the view model for marking tasks complete
     * @param taskViewModel the view model for the task being updated
     */
    public MarkTaskCompletePresenter(MarkTaskCompleteViewModel viewModel, TaskViewModel taskViewModel) {
        this.viewModel = viewModel;
        this.taskViewModel = taskViewModel;
    }


    /**
     * Prepares the view for a successful task completion.
     * Updates the state and notifies the relevant view models.
     *
     * @param outputData the output data from the mark task complete use case
     */
    @Override
    public void prepareSuccessView(MarkTaskCompleteOutputData outputData) {
        final MarkTaskCompleteState newState = new MarkTaskCompleteState();
        newState.setSuccess(true);
        newState.setError(null);
        viewModel.setState(newState);
        viewModel.firePropertyChanged("task completed");
        taskViewModel.firePropertyChanged();
    }


    /**
     * Prepares the view for a failed task completion attempt.
     * Updates the state and notifies the relevant view models.
     *
     * @param errorMessage the error message to display
     */
    @Override
    public void prepareFailView(String errorMessage) {
        final MarkTaskCompleteState newState = new MarkTaskCompleteState();
        newState.setSuccess(false);
        newState.setError(errorMessage);
        viewModel.setState(newState);
        viewModel.firePropertyChanged("task completed");
        taskViewModel.firePropertyChanged();
    }
}
