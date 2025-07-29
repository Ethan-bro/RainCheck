package use_case.EditTask;

/**
 * Output Boundary for the EditTask use case.
 */
public interface EditTaskOutputBoundary {

    /**
     * Prepares the success view for the MarkTaskComplete Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(EditTaskOutputData outputData);

    /**
     * Prepares the failure view for the MarkTaskComplete Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
