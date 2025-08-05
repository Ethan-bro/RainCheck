package use_case.DeleteTask;

import entity.TaskID;

/**
 * Output Data for the DeleteTask use case.
 */
public class DeleteTaskOutputData {

    private final TaskID taskId;
    private final boolean useCaseFailed;

    public DeleteTaskOutputData(TaskID taskId, boolean useCaseFailed) {
        this.taskId = taskId;
        this.useCaseFailed = useCaseFailed;
    }

    public TaskID getTaskId() {
        return taskId;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
