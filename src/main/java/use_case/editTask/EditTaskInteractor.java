package use_case.editTask;

import entity.Task;

/**
 * Interactor for the editTask use case
 */
public class EditTaskInteractor implements EditTaskInputBoundary {

    private final EditTaskDataAccessInterface dataAccess;
    private final EditTaskOutputBoundary presenter;

    public EditTaskInteractor(EditTaskDataAccessInterface dataAccess, EditTaskOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(String username, EditTaskInputData inputData) {
        Task updatedTask = inputData.getUpdatedTask();
        Task existingTask = dataAccess.getTaskById(username, updatedTask.getTaskInfo().getId());

        if (existingTask == null) {
            presenter.prepareFailView("Task not found.");
            return;
        }

        dataAccess.updateTask(username, updatedTask);

        EditTaskOutputData outputData = new EditTaskOutputData(
                updatedTask.getTaskInfo().getId(), false
        );
        presenter.prepareSuccessView(outputData);
    }
}