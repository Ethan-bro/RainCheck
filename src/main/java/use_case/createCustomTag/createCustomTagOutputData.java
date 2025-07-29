package use_case.createCustomTag;

import entity.CustomTag;

/**
 * The output data of the custom tag.
 */
public class createCustomTagOutputData {

    private final boolean UseCaseFailed;
    private final createCustomTagError errorType;
    private final String errorMessage;
    private final CustomTag createdTag;

    // failed UseCase constructor
    public createCustomTagOutputData(createCustomTagError errorType) {
        this.UseCaseFailed = true;
        this.errorType = errorType;
        this.errorMessage = errorType.getMessage();
        this.createdTag = null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // successful UseCase constructor
    public createCustomTagOutputData(CustomTag createdTag) {
        UseCaseFailed = false;
        errorType = null;
        errorMessage = null;
        this.createdTag = createdTag;
    }

    public boolean isUseCaseFailed() {
        return UseCaseFailed;
    }

    public CustomTag getCreatedTag() {
        return createdTag;
    }
}
