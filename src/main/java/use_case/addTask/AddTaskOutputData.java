package use_case.addTask;

import entity.Task;

public class AddTaskOutputData {

    private final boolean addTaskFailed;
    private final AddTaskError errorType;
    private final String errorMessage;
    private final Task newTask;

    public AddTaskOutputData(AddTaskError errorType) {
        this.addTaskFailed = true;
        this.errorType = errorType;
        this.errorMessage = errorType.getMessage();
        this.newTask = null;
    }

    public AddTaskOutputData(Task newTask) {
        this.addTaskFailed = false;
        this.errorType = null;
        this.errorMessage = null;
        this.newTask = newTask;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isAddTaskFailed() {
        return addTaskFailed;
    }

    public Task getNewTask() {
        return newTask;
    }
}
