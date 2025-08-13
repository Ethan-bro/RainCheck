package use_case.markTaskComplete;

import entity.TaskID;

/**
 * The Input Data for the markTaskComplete Use Case.
 */
public class MarkTaskCompleteInputData {

    private final String username;
    private final TaskID taskId;

    /**
     * Constructs a MarkTaskCompleteInputData object.
     *
     * @param username the username of the user marking the task complete
     * @param taskId the unique identifier of the task to mark complete
     */
    public MarkTaskCompleteInputData(String username, TaskID taskId) {
        this.username = username;
        this.taskId = taskId;
    }

    /**
     * Gets the username of the user marking the task complete.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the unique identifier of the task to mark complete.
     *
     * @return the task ID
     */
    public TaskID getTaskId() {
        return taskId;
    }
}
