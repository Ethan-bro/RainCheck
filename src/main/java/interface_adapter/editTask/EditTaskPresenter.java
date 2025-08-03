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
        EditTaskState newState = new EditTaskState();
        newState.setSuccess(true);
        viewModel.setState(newState);
        viewModel.firePropertyChange("task updated", null, null);

    }

    @Override
    public void prepareFailView(String errorMessage) {
        EditTaskState newState = new EditTaskState();
        newState.setSuccess(false);
        newState.setError(errorMessage);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }
}
