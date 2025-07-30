package use_case.MarkTaskComplete;

/**
 * The output boundary for the MarkTaskComplete Use Case.
 */
public interface MarkTaskCompleteOutputBoundary {

    /**
     * Prepares the success view for the MarkTaskComplete Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(MarkTaskCompleteOutputData outputData);

    /**
     * Prepares the failure view for the MarkTaskComplete Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
