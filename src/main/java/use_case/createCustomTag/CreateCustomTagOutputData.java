package use_case.createCustomTag;

import entity.CustomTag;

/**
 * The output data of the custom tag.
 */
public class CreateCustomTagOutputData {

    private final boolean useCaseFailed;
    private final CreateCustomTagError errorType;
    private final String errorMessage;
    private final CustomTag createdTag;


    /**
     * Constructs a CreateCustomTagOutputData representing a failed use case execution.
     *
     * @param errorType the type of error that occurred
     */
    public CreateCustomTagOutputData(CreateCustomTagError errorType) {
        this.useCaseFailed = true;
        this.errorType = errorType;
        this.errorMessage = errorType.getMessage();
        this.createdTag = null;
    }


    /**
     * Constructs a CreateCustomTagOutputData representing a successful use case execution.
     *
     * @param createdTag the newly created CustomTag
     */
    public CreateCustomTagOutputData(CustomTag createdTag) {
        useCaseFailed = false;
        errorType = null;
        errorMessage = null;
        this.createdTag = createdTag;
    }


    /**
     * Returns the error message if the use case failed.
     *
     * @return the error message, or null if the use case succeeded
     */
    public String getErrorMessage() {
        return errorMessage;
    }


    /**
     * Indicates whether the use case execution failed.
     *
     * @return true if the use case failed, false otherwise
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }


    /**
     * Returns the newly created CustomTag if the use case succeeded.
     *
     * @return the created CustomTag, or null if the use case failed
     */
    public CustomTag getCreatedTag() {
        return createdTag;
    }
}
