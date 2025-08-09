package use_case.edit_custom_tag;

/**
 * Output boundary interface for editing tags.
 */
public interface EditTagOutputBoundary {

    /**
     * Prepare the view to display on successful tag edit.
     *
     * @param successOutput the output data for a successful edit
     */
    void prepareSuccessView(EditTagOutputData successOutput);

    /**
     * Prepare the view to display on failed tag edit.
     *
     * @param successOutput the output data for a failed edit
     */
    void prepareFailView(EditTagOutputData successOutput);
}
