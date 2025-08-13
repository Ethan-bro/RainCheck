package use_case.deleteTask;

import java.io.IOException;

public class DeleteTaskInteractor implements DeleteTaskInputBoundary {

    private final DeleteTaskDataAccessInterface dataAccess;
    private final DeleteTaskOutputBoundary presenter;

    /**
     * Constructs a DeleteTaskInteractor.
     *
     * @param dataAccess the data access interface for tasks
     * @param presenter the output boundary to present results
     */
    public DeleteTaskInteractor(DeleteTaskDataAccessInterface dataAccess, DeleteTaskOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    /**
     * Executes the use case for deleting a task.
     *
     * @param inputData the input data containing username and task ID
     * @throws IOException if an I/O error occurs during deletion
     */
    @Override
    public void execute(DeleteTaskInputData inputData) throws IOException {
        if (dataAccess.getTaskById(inputData.username(), inputData.taskId()) == null) {
            presenter.prepareFailView("Task not found.");
        }
        else {
            dataAccess.deleteTask(inputData.username(), inputData.taskId());
            final DeleteTaskOutputData outputData = new DeleteTaskOutputData(inputData.taskId(), false);
            presenter.prepareSuccessView(outputData);
        }
    }
}
