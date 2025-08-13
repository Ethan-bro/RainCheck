package interface_adapter.deleteTask;

import java.io.IOException;

import entity.TaskID;
import use_case.deleteTask.DeleteTaskInputBoundary;
import use_case.deleteTask.DeleteTaskInputData;

public class DeleteTaskController {

    private final DeleteTaskInputBoundary deleteTaskInputInteractor;
    private String username;


    /**
     * Constructs a DeleteTaskController with the given input boundary interactor.
     *
     * @param deleteTaskInputInteractor the interactor responsible for deleting tasks
     */
    public DeleteTaskController(DeleteTaskInputBoundary deleteTaskInputInteractor) {
        this.deleteTaskInputInteractor = deleteTaskInputInteractor;
    }


    /**
     * Sets the username for the user performing the deletion.
     *
     * @param username the username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Deletes a task by its ID.
     *
     * @param taskId the ID of the task to delete
     * @throws IOException if an IO error occurs during deletion
     */
    public void deleteTask(TaskID taskId) throws IOException {
        final DeleteTaskInputData inputData = new DeleteTaskInputData(username, taskId);
        deleteTaskInputInteractor.execute(inputData);
    }
}
