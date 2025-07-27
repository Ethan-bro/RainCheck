package use_case.MarkTaskComplete;

import entity.Task;
import java.io.IOException;

/**
 * DAO for the MarkTaskComplete Use Case.
 */
public interface MarkTaskCompleteDataAccessInterface {

    /**
     * Retrieves the task with the given ID from the current user's task list.
     * @param username the username of the user whose tasks are being accessed
     * @param taskId the unique identifier of the task to retrieve
     * @return the task as a JsonObject if found, otherwise null
     */
    Task getTaskById(String username, int taskId) throws IOException;

    /**
     * Retrieves the task with the given ID from the current user's task list.
     * @param username the username of the user whose task list is being updated
     * @param task the modified task
     */
    void updateUsersTasks(String username, Task task);
}
