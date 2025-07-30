package use_case.DeleteTask;

/**
 * Input Boundary for actions which are related to deleting a task.
 */
public interface DeleteTaskInputBoundary {

    void execute(String username, DeleteTaskInputData inputData);
}
