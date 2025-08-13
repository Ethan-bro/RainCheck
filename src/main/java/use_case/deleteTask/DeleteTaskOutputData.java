package use_case.deleteTask;

import entity.TaskID;

/**
 * Output Data for the DeleteTask use case.
 */
public class DeleteTaskOutputData {

    private final TaskID taskId;
    private final boolean useCaseFailed;

    /**
     * Constructs a DeleteTaskOutputData object.
     *
     * @param taskId the unique identifier of the deleted task
     * @param useCaseFailed whether the use case failed
     */
    public DeleteTaskOutputData(TaskID taskId, boolean useCaseFailed) {
        this.taskId = taskId;
        this.useCaseFailed = useCaseFailed;
    }

    /**
     * Gets the unique identifier of the deleted task.
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
