package use_case.deleteTask;

/**
 * The output boundary for the DeleteTask Use Case.
 */
public interface DeleteTaskOutputBoundary {

    /**
    * Prepares the success view for the DeleteTask Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(DeleteTaskOutputData outputData);

    /**
     * Prepares the failure view for the DeleteTask Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
