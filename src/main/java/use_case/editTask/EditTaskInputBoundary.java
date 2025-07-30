package use_case.editTask;

/** The Input Boundary for actions which are related to editing a task.
 *
 */
public interface EditTaskInputBoundary {

    /** Executes the editTask use case.
     */
    void execute(String username, EditTaskInputData editTaskInputData);
}
