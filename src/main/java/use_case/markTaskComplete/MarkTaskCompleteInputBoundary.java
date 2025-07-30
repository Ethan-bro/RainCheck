package use_case.markTaskComplete;

import java.io.IOException;

/**
 * Input Boundary for actions which are related to marking task as complete.
 */
public interface MarkTaskCompleteInputBoundary {

    void execute(String username, MarkTaskCompleteInputData inputData);
}
