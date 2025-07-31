package interface_adapter.deleteTask;

import use_case.DeleteTask.DeleteTaskOutputBoundary;
import use_case.DeleteTask.DeleteTaskOutputData;

public class DeleteTaskPresenter implements DeleteTaskOutputBoundary {

    private final DeleteTaskViewModel viewModel;

    public DeleteTaskPresenter(DeleteTaskViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(DeleteTaskOutputData outputData) {
        DeleteTaskState newState = new DeleteTaskState();
        newState.setSuccess(true);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        DeleteTaskState newState = new DeleteTaskState();
        newState.setSuccess(false);
        newState.setError(errorMessage);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }
}
