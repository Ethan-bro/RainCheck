package use_case.deleteTask;

import entity.TaskID;

/**
 * Input Data for the DeleteTask use case.
 */
public class DeleteTaskInputData {

    private final String username;
    private final TaskID taskId;

    /**
     * Constructs the input data.
     * @param username the username associated with the request
     * @param taskId   the ID of the task to delete
     */
    public DeleteTaskInputData(String username, TaskID taskId) {
        this.username = username;
        this.taskId = taskId;
    }

    /**
     * Returns the username associated with the request.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the ID of the task to delete.
     * @return the task ID
     */
    public TaskID getTaskId() {
        return taskId;
    }
}
