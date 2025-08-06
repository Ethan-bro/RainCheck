package use_case.markTaskComplete;

/**
 * The output boundary for the markTaskComplete Use Case.
 */
public interface MarkTaskCompleteOutputBoundary {

    /**
     * Prepares the success view for the markTaskComplete Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(MarkTaskCompleteOutputData outputData);

    /**
     * Prepares the failure view for the markTaskComplete Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
