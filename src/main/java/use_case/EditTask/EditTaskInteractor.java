package use_case.EditTask;

import java.io.IOException;

/**
 * Interactor for the EditTask use case
 */
public class EditTaskInteractor implements EditTaskInputBoundary {

    private final EditTaskDataAccessInterface dataAccess;
    private final EditTaskOutputBoundary presenter;

    public EditTaskInteractor(EditTaskDataAccessInterface dataAccess, EditTaskOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(EditTaskInputData inputData) throws IOException {
        try {
            String username = inputData.getUsername();
            Task updatedTask = inputData.getUpdatedTask();

            Task existingTask = dataAccess.getTaskById(username, updatedTask.getId());

            if (existingTask == null) {
                presenter.prepareFailView("Task not found.");
                return;
            }

            dataAccess.updateUsersTasks(username, updatedTask);

            EditTaskOutputData outputData = new EditTaskOutputData(updatedTask.getId(), false);
            presenter.prepareSuccessView(outputData);

        } catch (IOException e) {
            presenter.prepareFailView("Failed to edit task due to an error.");
        }
    }
}
