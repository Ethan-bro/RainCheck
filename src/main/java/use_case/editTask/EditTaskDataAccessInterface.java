package use_case.editTask;

import entity.Task;
import entity.TaskID;

/**
 * Data access interface for editing tasks.
 */
public interface EditTaskDataAccessInterface {

    /**
     * Retrieves a task by its ID and associated email.
     * @param email the email associated with the task
     * @param id the task ID
     * @return the matching task, if any
     */
    Task getTaskByIdAndEmail(String email, TaskID id);

    /**
     * Retrieves a task by its ID and username.
     * @param username the username associated with the task
     * @param taskId   the task ID
     * @return the matching task, if any
     */
    Task getTaskById(String username, TaskID taskId);

    /**
     * Updates a task for the specified user.
     * @param username the username associated with the task
     * @param task the updated task
     */
    void updateTask(String username, Task task);

}
