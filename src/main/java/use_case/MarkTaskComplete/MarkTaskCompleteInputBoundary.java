package use_case.MarkTaskComplete;

/**
 * Input Boundary for actions which are related to marking task as complete.
 */
public interface MarkTaskCompleteInputBoundary {

    /**
     * Executes the MarkTaskComplete use case.
     * @param markTaskCompleteInputData the input data
     */
    void execute(MarkTaskCompleteInputData markTaskCompleteInputData);
}
