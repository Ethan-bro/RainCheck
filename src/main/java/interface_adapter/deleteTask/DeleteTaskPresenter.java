package interface_adapter.deleteTask;

import use_case.deleteTask.DeleteTaskOutputBoundary;
import use_case.deleteTask.DeleteTaskOutputData;

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
    }

    @Override
    public void prepareFailView(String errorMessage) {
        DeleteTaskState newState = new DeleteTaskState();
        newState.setSuccess(false);
        newState.setError(errorMessage);
        viewModel.setState(newState);
    }
}
