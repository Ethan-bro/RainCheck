package use_case.markTaskComplete;

import entity.TaskID;

/**
 * The Input Data for the markTaskComplete Use Case.
 */
public class MarkTaskCompleteInputData {

    private final TaskID taskId;

    public MarkTaskCompleteInputData(TaskID taskId) {
        this.taskId = taskId;
    }

    public TaskID getTaskId() {
        return taskId;
    }
}
