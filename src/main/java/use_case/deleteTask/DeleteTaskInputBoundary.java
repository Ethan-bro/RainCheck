package use_case.deleteTask;

import java.io.IOException;

/**
 * Input Boundary for actions which are related to deleting a task.
 */
public interface DeleteTaskInputBoundary {

    void execute(String username, DeleteTaskInputData inputData);
}
