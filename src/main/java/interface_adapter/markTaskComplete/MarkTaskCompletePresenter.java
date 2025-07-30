package interface_adapter.markTaskComplete;

import use_case.markTaskComplete.MarkTaskCompleteOutputBoundary;
import use_case.markTaskComplete.MarkTaskCompleteOutputData;

public class MarkTaskCompletePresenter implements MarkTaskCompleteOutputBoundary {

    private final MarkTaskCompleteViewModel viewModel;

    public MarkTaskCompletePresenter(MarkTaskCompleteViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(MarkTaskCompleteOutputData outputData) {
        MarkTaskCompleteState newState = new MarkTaskCompleteState();
        newState.setSuccess(true);
        newState.setError(null);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        MarkTaskCompleteState newState = new MarkTaskCompleteState();
        newState.setSuccess(false);
        newState.setError(errorMessage);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }
}
