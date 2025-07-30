package use_case.DeleteTask;

/**
 * Output Data for the DeleteTask use case.
 */
public class DeleteTaskOutputData {

    private final int taskId;
    private final boolean useCaseFailed;

    public DeleteTaskOutputData(int taskId, boolean useCaseFailed) {
        this.taskId = taskId;
        this.useCaseFailed = useCaseFailed;
    }

    public int getTaskId() {
        return taskId;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
