package use_case.editTask;

import entity.Task;
import entity.TaskID;

/**
 * Data Access Interface for editing tasks.
 */
public interface EditTaskDataAccessInterface {

    /**
     * Retrieves a task by its ID and the user's email.
     *
     * @param email the email of the user who owns the task
     * @param id the unique identifier of the task
     * @return the Task object if found, otherwise null
     */
    Task getTaskByIdAndEmail(String email, TaskID id);

    /**
     * Retrieves a task by its ID and the username.
     *
     * @param username the username of the user who owns the task
     * @param taskId the unique identifier of the task
     * @return the Task object if found, otherwise null
     */
    Task getTaskById(String username, TaskID taskId);

    /**
     * Updates the task information for the specified user.
     *
     * @param username the username of the user who owns the task
     * @param task the Task object with updated information
     */
    void updateTask(String username, Task task);
}
