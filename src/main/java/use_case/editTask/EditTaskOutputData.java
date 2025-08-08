package use_case.editTask;

import entity.TaskID;

/**
 * Output Data for the editTask use case.
 */
public class EditTaskOutputData {

    private final TaskID taskId;
    private final boolean useCaseFailed;

    /**
     * Constructs the output data.
     * @param taskId the ID of the edited task
     * @param useCaseFailed true if the use case failed, false otherwise
     */
    public EditTaskOutputData(TaskID taskId, boolean useCaseFailed) {
        this.taskId = taskId;
        this.useCaseFailed = useCaseFailed;
    }

    /**
     * Returns the ID of the edited task.
     * @return the task ID
     */
    public TaskID getTaskId() {
        return taskId;
    }

    /**
     * Returns whether the use case failed.
     * @return true if the use case failed, false otherwise
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
