package use_case.markTaskComplete;

import entity.Task;
import entity.TaskID;

/**
 * DAO for the markTaskComplete Use Case.
 */
public interface MarkTaskCompleteDataAccessInterface {

    /**
     * Retrieves the task with the given ID from the current user's task list.
     * @param taskId the unique identifier of the task to retrieve
     * @return the task as a JsonObject if found, otherwise null
     */
    Task getTaskById(String username, TaskID taskId);

    /**
     * Retrieves the task with the given ID from the current user's task list.
     *
     * @param task the modified task
     */
    void updateTask(String username, Task task);

    void markAsComplete(String username, TaskID taskId);
}
