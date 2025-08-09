package use_case.deleteCustomTag;

/**
 * Output boundary interface for deleting a custom tag.
 * Defines how to prepare the success view after deletion.
 */
public interface DeleteCustomTagOutputBoundary {

    /**
     * Prepares the success view with the given output data.
     *
     * @param successOutput the output data representing successful deletion
     */
    void prepareSuccessView(DeleteCustomTagOutputData successOutput);

}
