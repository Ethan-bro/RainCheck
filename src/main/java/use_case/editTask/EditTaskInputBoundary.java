package use_case.editTask;

/**
 * The Input Boundary for actions which are related to editing a task.
 */
public interface EditTaskInputBoundary {

    /**
     * Executes the editTask use case.
     *
     * @param username the username of the user editing the task
     * @param editTaskInputData the input data for the task edit
     */
    void execute(String username, EditTaskInputData editTaskInputData);
}
