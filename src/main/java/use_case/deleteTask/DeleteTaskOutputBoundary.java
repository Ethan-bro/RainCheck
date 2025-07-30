package use_case.deleteTask;

/**
 * The output boundary for the deleteTask Use Case.
 */
public interface DeleteTaskOutputBoundary {

    /**
    * Prepares the success view for the deleteTask Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(DeleteTaskOutputData outputData);

    /**
     * Prepares the failure view for the deleteTask Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
