package use_case.markTaskComplete;

import entity.TaskID;

/**
 * Output Data for the markTaskComplete Use Case.
 */
public class MarkTaskCompleteOutputData {
    private final TaskID taskId;
    private final boolean useCaseFailed;

    /**
     * Constructs the output data.
     * @param taskId the ID of the task
     * @param useCaseFailed true if the use case failed, false otherwise
     */
    public MarkTaskCompleteOutputData(TaskID taskId, boolean useCaseFailed) {
        this.taskId = taskId;
        this.useCaseFailed = useCaseFailed;
    }

    /**
     * Returns the ID of the task.
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
