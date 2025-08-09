package use_case.deleteTask;

import entity.Task;
import entity.TaskID;

import java.io.IOException;

/**
 * Data Access Object interface for the DeleteTask use case.
 */
public interface DeleteTaskDataAccessInterface {

    /**
     * Retrieves the task with the given ID from the specified user's task list.
     *
     * @param username the username of the user whose tasks are being accessed
     * @param taskId the unique identifier of the task to retrieve
     * @return the Task object if found, otherwise null
     * @throws IOException if an I/O error occurs during retrieval
     */
    Task getTaskById(String username, TaskID taskId) throws IOException;

    /**
     * Deletes the task with the specified ID from the user's task list.
     *
     * @param username the username of the user whose task is to be deleted
     * @param taskId the unique identifier of the task to delete
     */
    void deleteTask(String username, TaskID taskId);
}
