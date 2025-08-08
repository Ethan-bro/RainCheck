package use_case.editTask;

/**
 * The Input Boundary for actions which are related to editing a task.
 */
public interface EditTaskInputBoundary {

    /**
     * Executes the editTask use case.
     * @param username the user's username
     * @param editTaskInputData the input data
     */
    void execute(String username, EditTaskInputData editTaskInputData);
}
