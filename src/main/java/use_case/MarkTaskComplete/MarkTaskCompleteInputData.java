package use_case.MarkTaskComplete;

/**
 * The Input Data for the MarkTaskComplete Use Case.
 */
public class MarkTaskCompleteInputData {

    private final String username;
    private final int taskId;

    public MarkTaskCompleteInputData(String username, int taskId) {
        this.username = username;
        this.taskId = taskId;
    }

    public String getUsername() {
        return username;
    }

    public int getTaskId() {
        return taskId;
    }
}
