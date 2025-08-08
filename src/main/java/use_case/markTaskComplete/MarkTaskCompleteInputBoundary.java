package use_case.markTaskComplete;

/**
 * Input Boundary for actions which are related to marking task as complete.
 */
public interface MarkTaskCompleteInputBoundary {

    /**
     * Executes the mark task complete use case.
     * @param username the username of the user
     * @param inputData the input data for the use case
     */
    void execute(String username, MarkTaskCompleteInputData inputData);
}
