package use_case.deleteTask;

import java.io.IOException;

public class DeleteTaskInteractor implements DeleteTaskInputBoundary {

    private final DeleteTaskDataAccessInterface dataAccess;
    private final DeleteTaskOutputBoundary presenter;

    public DeleteTaskInteractor(DeleteTaskDataAccessInterface dataAccess, DeleteTaskOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteTaskInputData inputData) throws IOException {
        // Confirm the task exists first
        if (dataAccess.getTaskById(inputData.getUsername(), inputData.getTaskId()) == null) {
            presenter.prepareFailView("Task not found.");
            return;
        }

        // Delete the task
        dataAccess.deleteTask(inputData.getUsername(), inputData.getTaskId());

        // Success response
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(inputData.getTaskId(), false);
        presenter.prepareSuccessView(outputData);

    }
}
