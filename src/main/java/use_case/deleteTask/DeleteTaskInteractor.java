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
