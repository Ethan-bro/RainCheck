package use_case.MarkTaskComplete;

import entity.TaskID;

/**
 * The Input Data for the MarkTaskComplete Use Case.
 */
public class MarkTaskCompleteInputData {

    private final String username;
    private final TaskID taskId;

    public MarkTaskCompleteInputData(String username, TaskID taskId) {
        this.username = username;
        this.taskId = taskId;
    }

    public String getUsername() {
        return username;
    }

    public TaskID getTaskId() {
        return taskId;
    }
}
