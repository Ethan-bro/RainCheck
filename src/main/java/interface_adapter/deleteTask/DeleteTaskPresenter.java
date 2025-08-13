package interface_adapter.deleteTask;

import interface_adapter.task.TaskViewModel;

import use_case.deleteTask.DeleteTaskOutputBoundary;
import use_case.deleteTask.DeleteTaskOutputData;

public class DeleteTaskPresenter implements DeleteTaskOutputBoundary {

    private final DeleteTaskViewModel viewModel;
    private final TaskViewModel taskViewModel;

    /**
     * Constructs a DeleteTaskPresenter with the given view models.
     *
     * @param viewModel the view model for delete task state
     * @param taskViewModel the view model for the task being deleted
     */
    public DeleteTaskPresenter(DeleteTaskViewModel viewModel, TaskViewModel taskViewModel) {
        this.viewModel = viewModel;
        this.taskViewModel = taskViewModel;
    }

    /**
     * Prepares the view for a successful task deletion.
     * Updates the state and notifies the relevant view models.
     *
     * @param outputData the output data from the delete task use case
     */
    @Override
    public void prepareSuccessView(DeleteTaskOutputData outputData) {
        final DeleteTaskState newState = new DeleteTaskState();
        newState.setSuccess(true);
        viewModel.setState(newState);
        viewModel.firePropertyChanged("task deleted");
        taskViewModel.getTask().getTaskInfo().setIsDeleted("Yes");
        taskViewModel.firePropertyChanged();
    }

    /**
     * Prepares the view for a failed task deletion.
     * Updates the state and notifies the relevant view models.
     *
     * @param errorMessage the error message to display
     */
    @Override
    public void prepareFailView(String errorMessage) {
        final DeleteTaskState newState = new DeleteTaskState();
        newState.setSuccess(false);
        newState.setError(errorMessage);
        viewModel.setState(newState);
        viewModel.firePropertyChanged("task deleted");
    }
}
