package use_case.deleteTask;

import java.io.IOException;

/**
 * Input Boundary for actions related to deleting a task.
 */
public interface DeleteTaskInputBoundary {

    /**
     * Executes the DeleteTask use case.
     *
     * @param deleteTaskInputData the input data for deleting a task
     * @throws IOException if an I/O error occurs during the delete operation
     */
    void execute(DeleteTaskInputData deleteTaskInputData) throws IOException;
}
