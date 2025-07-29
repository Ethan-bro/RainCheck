package use_case.EditTask;

import java.io.IOException;

/** The Input Boundary for actions which are related to editing a task.
 *
 */
public interface EditTaskInputBoundary {

    /** Executes the EditTask use case.
     * @param EditTaskInputData editTaskInputData
     */
    void execute(EditTaskInputData editTaskInputData) throws IOException;
}
