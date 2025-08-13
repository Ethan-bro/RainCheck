package use_case.addTask;

import entity.Task;

public class AddTaskOutputData {

    private final boolean addTaskFailed;
    private final AddTaskError errorType;
    private final String errorMessage;
    private final Task newTask;

    /**
     * Constructs an AddTaskOutputData representing a failed add task operation.
     *
     * @param errorType the type of error that occurred
     */
    public AddTaskOutputData(AddTaskError errorType) {
        this.addTaskFailed = true;
        this.errorType = errorType;
        this.errorMessage = errorType.getMessage();
        this.newTask = null;
    }

    /**
     * Constructs an AddTaskOutputData representing a successful add task operation.
     *
     * @param newTask the newly created Task
     */
    public AddTaskOutputData(Task newTask) {
        this.addTaskFailed = false;
        this.errorType = null;
        this.errorMessage = null;
        this.newTask = newTask;
    }

    /**
     * Returns the error message if the add task operation failed.
     *
     * @return the error message, or null if the operation succeeded
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Indicates whether the add task operation failed.
     *
     * @return true if the operation failed, false otherwise
     */
    public boolean isAddTaskFailed() {
        return addTaskFailed;
    }

    /**
     * Returns the newly created Task if the operation succeeded.
     *
     * @return the new Task, or null if the operation failed
     */
    public Task getNewTask() {
        return newTask;
    }
}
