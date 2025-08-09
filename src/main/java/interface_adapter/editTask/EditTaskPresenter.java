package interface_adapter.editTask;

import use_case.editTask.EditTaskOutputBoundary;
import use_case.editTask.EditTaskOutputData;

public class EditTaskPresenter implements EditTaskOutputBoundary {

    private final EditTaskViewModel viewModel;

    public EditTaskPresenter(EditTaskViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(EditTaskOutputData outputData) {
        final EditTaskState newState = new EditTaskState();
        newState.setSuccess(true);
        viewModel.setState(newState);
        viewModel.firePropertyChange("task updated", null, null);

    }

    @Override
    public void prepareFailView(String errorMessage) {
        final EditTaskState newState = new EditTaskState();
        newState.setSuccess(false);
        newState.setError("task failed to update");
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }
}
