package use_case.addTask;

import entity.Task;

public class AddTaskOutputData {

    private boolean AddTaskFailed;
    private final AddTaskError errortype;
    private final String errorMessage;
    private final Task newTask;

    public AddTaskOutputData(AddTaskError errortype) {
        this.AddTaskFailed = true;
        this.errortype = errortype;
        this.errorMessage = errortype.getMessage();
        this.newTask = null;
    }

    public AddTaskOutputData(Task newTask) {
        this.AddTaskFailed = false;
        this.errortype = null;
        this.errorMessage = null;
        this.newTask = newTask;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isAddTaskFailed() {
        return AddTaskFailed;
    }

    public Task getNewTask() {
        return newTask;
    }
}
