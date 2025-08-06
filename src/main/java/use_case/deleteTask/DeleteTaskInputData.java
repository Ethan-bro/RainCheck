package use_case.deleteTask;


import entity.TaskID;

/**
 * Input Data for the DeleteTask use case.
 */
public class DeleteTaskInputData {

    private final String username;
    private final TaskID taskId;

    public DeleteTaskInputData(String username, TaskID taskId) {
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
