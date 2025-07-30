package use_case.editTask;

/**
 * Output Boundary for the editTask use case.
 */
public interface EditTaskOutputBoundary {

    /**
     * Prepares the success view for the markTaskComplete Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(EditTaskOutputData outputData);

    /**
     * Prepares the failure view for the markTaskComplete Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
