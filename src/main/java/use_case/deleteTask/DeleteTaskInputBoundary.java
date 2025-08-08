package use_case.deleteTask;

import java.io.IOException;

/**
 * Input Boundary for actions which are related to deleting a task.
 */
public interface DeleteTaskInputBoundary {

    /**
     * Executes the DeleteTask use case.
     * @param deleteTaskInputData the input data
     * @throws IOException IO exception
     */
    void execute(DeleteTaskInputData deleteTaskInputData) throws IOException;
}
