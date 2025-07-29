package use_case.EditTask;

import entity.Task;

/** The Input Data for editing a task.
 */
public class EditTaskInputData {

    private final String username;
    private final Task updatedTask;

    public EditTaskInputData(String username, Task updatedTask) {
        this.username = username;
        this.updatedTask = updatedTask;
    }

    public String getUsername() {
        return username;
    }

    public Task getUpdatedTask() {
        return updatedTask;
    }
}
