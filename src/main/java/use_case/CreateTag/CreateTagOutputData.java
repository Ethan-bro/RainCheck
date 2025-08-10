package use_case.CreateTag;

import entity.CustomTag;

/**
 * The output data of the custom tag.
 */
public class CreateTagOutputData {

    private final boolean UseCaseFailed;
    private final TagErrors errorType;
    private final String errorMessage;
    private final CustomTag createdTag;

    // failed UseCase constructor
    public CreateTagOutputData(TagErrors errorType) {
        this.UseCaseFailed = true;
        this.errorType = errorType;
        this.errorMessage = errorType.getMessage();
        this.createdTag = null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // successful UseCase constructor
    public CreateTagOutputData(CustomTag createdTag) {
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
