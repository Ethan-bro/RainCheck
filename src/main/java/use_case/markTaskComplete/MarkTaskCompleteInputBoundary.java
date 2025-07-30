package use_case.markTaskComplete;

/**
 * Input Boundary for actions which are related to marking task as complete.
 */
public interface MarkTaskCompleteInputBoundary {

    /**
     * Executes the markTaskComplete use case.
     */
    void execute(String username, MarkTaskCompleteInputData inputData);
}
