package use_case.addTask;

/**
 * Defines the output boundary for the Add Task use case.
 * This interface is responsible for preparing the view models
 * for both successful and failed attempts to add a task.
 */
public interface AddTaskOutputBoundary {

    /**
     * Prepares the view to present the failure state when adding a task fails.
     * @param failedOutputData the output data containing details about the failure
     */
    void prepareFailView(AddTaskOutputData failedOutputData);

    /**
     * Prepares the view to present the success state when a task is added successfully.
     * @param successOutputData the output data containing details about the newly added task
     */
    void prepareSuccessView(AddTaskOutputData successOutputData);
}
