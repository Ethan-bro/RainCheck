package use_case.edit_custom_tag;

import entity.CustomTag;

/**
 * Output data for the Edit Tag use case.
 */
public class EditTagOutputData {

    private final boolean useCaseFailed;
    private final String errorMessage;
    private final CustomTag createdTag;

    /**
     * Constructor for a failed use case result.
     *
     * @param errorMessage the error message explaining the failure
     */
    public EditTagOutputData(String errorMessage) {
        this.useCaseFailed = true;
        this.errorMessage = errorMessage;
        this.createdTag = null;
    }

    /**
     * Constructor for a successful use case result.
     *
     * @param createdTag the tag created/edited successfully
     */
    public EditTagOutputData(CustomTag createdTag) {
        this.useCaseFailed = false;
        this.errorMessage = null;
        this.createdTag = createdTag;
    }

    /**
     * Returns whether the use case failed.
     *
     * @return true if the use case failed, false otherwise
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }

    /**
     * Returns the error message if use case failed.
     *
     * @return error message string, or null if none
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Returns the created or edited tag if successful.
     *
     * @return the created CustomTag, or null if failure
     */
    public CustomTag getCreatedTag() {
        return createdTag;
    }
}
