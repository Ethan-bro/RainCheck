package use_case.EditTask;

/**
 * Output Data for the EditTask use case.
 */
public class EditTaskOutputData {

    private final int taskId;
    private final boolean useCaseFailed;

    public EditTaskOutputData(int taskId, boolean useCaseFailed) {
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
