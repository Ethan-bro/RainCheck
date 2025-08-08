package interface_adapter.markTaskComplete;

import interface_adapter.task.TaskViewModel;
import use_case.markTaskComplete.MarkTaskCompleteOutputBoundary;
import use_case.markTaskComplete.MarkTaskCompleteOutputData;

public class MarkTaskCompletePresenter implements MarkTaskCompleteOutputBoundary {

    private final MarkTaskCompleteViewModel viewModel;
    private final TaskViewModel taskViewModel;

    public MarkTaskCompletePresenter(MarkTaskCompleteViewModel viewModel, TaskViewModel taskViewModel) {
        this.viewModel = viewModel;
        this.taskViewModel = taskViewModel;
    }

    @Override
    public void prepareSuccessView(MarkTaskCompleteOutputData outputData) {
        MarkTaskCompleteState newState = new MarkTaskCompleteState();
        newState.setSuccess(true);
        newState.setError(null);
        viewModel.setState(newState);
        viewModel.firePropertyChanged("task completed");
        taskViewModel.firePropertyChanged();

    }

    @Override
    public void prepareFailView(String errorMessage) {
        MarkTaskCompleteState newState = new MarkTaskCompleteState();
        newState.setSuccess(false);
        newState.setError(errorMessage);
        viewModel.setState(newState);
        viewModel.firePropertyChanged("task completed");
        taskViewModel.firePropertyChanged();
    }
}
