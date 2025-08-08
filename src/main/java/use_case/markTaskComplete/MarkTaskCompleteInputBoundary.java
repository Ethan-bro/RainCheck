package use_case.markTaskComplete;

/**
 * Input boundary interface for use cases related to marking a task as complete.
 * Implementations handle the business logic when a user requests to mark a task complete.
 */
public interface MarkTaskCompleteInputBoundary {

    /**
     * Executes the use case to mark a task as complete for the given user.
     *
     * @param username the username of the user marking the task complete
     * @param inputData the input data containing information about the task to complete
     */
    void execute(String username, MarkTaskCompleteInputData inputData);
}
