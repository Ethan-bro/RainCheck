package use_case.MarkTaskComplete;

import java.io.IOException;

/**
 * Input Boundary for actions which are related to marking task as complete.
 */
public interface MarkTaskCompleteInputBoundary {

    /**
     * Executes the MarkTaskComplete use case.
     * @param markTaskCompleteInputData the input data
     */
    void execute(MarkTaskCompleteInputData markTaskCompleteInputData) throws IOException;
}
