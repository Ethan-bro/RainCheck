package use_case.markTaskComplete;

import entity.TaskID;

/**
 * The Input Data for the markTaskComplete Use Case.
 */
public class MarkTaskCompleteInputData {

    private final String username;
    private final TaskID taskId;

    /**
     * Constructs the input data.
     * @param username the username associated with the task
     * @param taskId the ID of the task to mark complete
     */
    public MarkTaskCompleteInputData(String username, TaskID taskId) {
        this.username = username;
        this.taskId = taskId;
    }

    /**
     * Returns the username associated with the task.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the ID of the task to mark complete.
     * @return the task ID
     */
    public TaskID getTaskId() {
        return taskId;
    }
}
