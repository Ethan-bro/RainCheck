package interface_adapter.deleteTask;

import interface_adapter.task.TaskViewModel;
import use_case.deleteTask.DeleteTaskOutputBoundary;
import use_case.deleteTask.DeleteTaskOutputData;

public class DeleteTaskPresenter implements DeleteTaskOutputBoundary {

    private final DeleteTaskViewModel viewModel;
    private final TaskViewModel taskViewModel;

    public DeleteTaskPresenter(DeleteTaskViewModel viewModel, TaskViewModel taskViewModel) {
        this.viewModel = viewModel;
        this.taskViewModel = taskViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteTaskOutputData outputData) {
        DeleteTaskState newState = new DeleteTaskState();
        newState.setSuccess(true);
        viewModel.setState(newState);
        viewModel.firePropertyChanged("task deleted");
        taskViewModel.getTask().getTaskInfo().setIsDeleted("Yes");
        taskViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        DeleteTaskState newState = new DeleteTaskState();
        newState.setSuccess(false);
        newState.setError(errorMessage);
        viewModel.setState(newState);
        viewModel.firePropertyChanged("task deleted");
    }
}
