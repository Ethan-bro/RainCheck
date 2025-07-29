package entity;

public class Task {
    private final TaskInfo taskInfo;

    public Task(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public TaskInfo getTaskInfo() {
        return taskInfo;
    }
}
