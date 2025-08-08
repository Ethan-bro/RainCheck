package use_case.editTask;

import entity.Task;

/**
 * The Input Data for editing a task.
 */
public class EditTaskInputData {

    private final String username;
    private final Task updatedTask;

    public EditTaskInputData(String username, Task updatedTask) {
        this.username = username;
        this.updatedTask = updatedTask;
    }

    /**
     * Returns user's username.
     * @return user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the updated task.
     * @return the updated task
     */
    public Task getUpdatedTask() {
        return updatedTask;
    }
}
