package use_case.DeleteTask;

import entity.Task;
import entity.TaskID;

import java.io.IOException;

/**
 * DAO for the DeleteTask Use Case.
 */
public interface DeleteTaskDataAccessInterface {

    /**
     * Retrieves the task with the given ID from the current user's task list.
     * @param username the username of the user whose tasks are being accessed
     * @param taskId the unique identifier of the task to retrieve
     * @return the task as a JsonObject if found, otherwise null
     */
    Task getTaskById(String username, TaskID taskId) throws IOException;

    void deleteTask(String username, TaskID taskId);
}
