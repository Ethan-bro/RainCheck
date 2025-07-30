package use_case.deleteTask;

import entity.TaskID;

/**
 * Input Data for the deleteTask use case.
 */
public class DeleteTaskInputData {

    private final TaskID taskId;

    public DeleteTaskInputData(TaskID taskId) {
        this.taskId = taskId;
    }

    public TaskID getTaskId() {
        return taskId;
    }
}
