package use_case.DeleteTask;

import java.io.IOException;

/**
 * Input Boundary for actions which are related to deleting a task.
 */
public interface DeleteTaskInputBoundary {

    /**
     * Executes the DeleteTask use case.
     * @param deleteTaskInputData the input data
     */
    void execute(DeleteTaskInputData deleteTaskInputData) throws IOException;
}
