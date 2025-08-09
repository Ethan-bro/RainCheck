package use_case.deleteCustomTag;

/**
 * Input boundary for the Delete Custom Tag use case.
 */
public interface DeleteCustomTagInputBoundary {

    /**
     * Executes the delete custom tag use case with the given input data.
     *
     * @param input the input data for deleting a custom tag
     */
    void execute(DeleteCustomTagInputData input);
}
