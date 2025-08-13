package interface_adapter.editTask;

import use_case.editTask.EditTaskOutputBoundary;
import use_case.editTask.EditTaskOutputData;

public class EditTaskPresenter implements EditTaskOutputBoundary {

    private final EditTaskViewModel viewModel;

    /**
     * Constructs an EditTaskPresenter with the given view model.
     *
     * @param viewModel the view model for editing tasks
     */
    public EditTaskPresenter(EditTaskViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Prepares the view for a successful task edit.
     * Updates the state and notifies the relevant view model.
     *
     * @param outputData the output data from the edit task use case
     */
    @Override
    public void prepareSuccessView(EditTaskOutputData outputData) {
        final EditTaskState newState = new EditTaskState();
        newState.setSuccess(true);
        viewModel.setState(newState);
        viewModel.firePropertyChange("task updated", null, null);
    }

    /**
     * Prepares the view for a failed task edit.
     * Updates the state and notifies the relevant view model.
     *
     * @param errorMessage the error message to display
     */
    @Override
    public void prepareFailView(String errorMessage) {
        final EditTaskState newState = new EditTaskState();
        newState.setSuccess(false);
        newState.setError("task failed to update");
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }
}
