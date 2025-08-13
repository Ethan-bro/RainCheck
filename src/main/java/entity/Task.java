package entity;

public class Task {
    private TaskInfo taskInfo;

    /**
     * Constructs a Task with the given TaskInfo.
     * @param taskInfo the task information
     */
    public Task(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    /**
     * Gets the task information.
     * @return the TaskInfo
     */
    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    /**
     * Sets the task information.
     * @param taskInfo the TaskInfo to set
     */
    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }
}
