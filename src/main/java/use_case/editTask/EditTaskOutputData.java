package use_case.editTask;

import entity.TaskID;

/**
 * Output Data for the editTask use case.
 */
public class EditTaskOutputData {

    private final TaskID taskId;
    private final boolean useCaseFailed;

    /**
     * Constructs an EditTaskOutputData object.
     *
     * @param taskId the unique identifier of the edited task
     * @param useCaseFailed whether the use case failed
     */
    public EditTaskOutputData(TaskID taskId, boolean useCaseFailed) {
        this.taskId = taskId;
        this.useCaseFailed = useCaseFailed;
    }

    /**
     * Gets the unique identifier of the edited task.
     *
     * @return the task ID
     */
    public TaskID getTaskId() {
        return taskId;
    }

    /**
     * Indicates whether the use case failed.
     *
     * @return true if the use case failed, false otherwise
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
