package use_case.addTask;

/**
 * Input boundary interface for the Add Task use case.
 * Defines the method to execute adding a task with the given input data and username.
 */
public interface AddTaskInputBoundary {

    /**
     * Executes the use case to add a task.
     *
     * @param addTaskInputData the data required to create a new task
     * @param username the username of the user adding the task
     */
    void execute(AddTaskInputData addTaskInputData, String username);
}
