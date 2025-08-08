package interface_adapter.deleteTask;

import java.io.IOException;

import entity.TaskID;
import use_case.deleteTask.DeleteTaskInputBoundary;
import use_case.deleteTask.DeleteTaskInputData;

public class DeleteTaskController {
    private final DeleteTaskInputBoundary deleteTaskInputInteractor;
    private String username;

    public DeleteTaskController(DeleteTaskInputBoundary deleteTaskInputInteractor) {
        this.deleteTaskInputInteractor = deleteTaskInputInteractor;
    }

    /**
     * Sets the username for the current session context.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Deletes the user's task.
     * @param taskId the ID of the task
     * @throws IOException IO exception
     */
    public void deleteTask(TaskID taskId) throws IOException {
        DeleteTaskInputData inputData = new DeleteTaskInputData(username, taskId);
        deleteTaskInputInteractor.execute(inputData);
    }
}
