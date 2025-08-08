package use_case.deleteTask;

import java.io.IOException;

import entity.Task;
import entity.TaskID;

/**
 * DAO for the DeleteTask Use Case.
 */
public interface DeleteTaskDataAccessInterface {

    /**
     * Retrieves the task with the given ID from the current user's task list.
     * @param username the username of the user whose tasks are being accessed
     * @param taskId the unique identifier of the task to retrieve
     * @return the task as a JsonObject if found, otherwise null
     * @throws IOException IO exception
     */
    Task getTaskById(String username, TaskID taskId) throws IOException;

    /**
     * Deletes the task with given taskId.
     * @param username the user name of the user whose tasks are being accessed
     * @param taskId the unique identifier of the task to delete
     */
    void deleteTask(String username, TaskID taskId);
}
