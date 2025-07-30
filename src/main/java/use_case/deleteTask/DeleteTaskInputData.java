package use_case.DeleteTask;


/**
 * Input Data for the DeleteTask use case.
 */
public class DeleteTaskInputData {

    private final String username;
    private final int taskId;

    public DeleteTaskInputData(String username, int taskId) {
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
