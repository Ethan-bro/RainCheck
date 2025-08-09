package use_case.markTaskComplete;

import entity.Task;
import entity.TaskID;

/**
 * Data Access Object interface for the MarkTaskComplete use case.
 * Defines methods to retrieve, update, and mark tasks as complete for a given user.
 */
public interface MarkTaskCompleteDataAccessInterface {

    /**
     * Retrieves the task with the specified ID for the given user.
     *
     * @param username the username of the user whose tasks are being accessed
     * @param taskId the unique identifier of the task to retrieve
     * @return the Task object if found; otherwise, null
     */
    Task getTaskById(String username, TaskID taskId);

    /**
     * Updates the given task in the user's task list.
     *
     * @param username the username of the user whose task list is being updated
     * @param task the modified Task object to update
     */
    void updateTask(String username, Task task);

    /**
     * Marks the specified task as complete for the given user.
     *
     * @param username the username of the user
     * @param taskId the unique identifier of the task to mark complete
     */
    void markAsComplete(String username, TaskID taskId);
}
