package use_case.editTask;

import entity.TaskID;

/**
 * Output Data for the editTask use case.
 */
public class EditTaskOutputData {

    private final TaskID taskId;
    private final boolean useCaseFailed;

    public EditTaskOutputData(TaskID taskId, boolean useCaseFailed) {
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
