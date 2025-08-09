package use_case.CreateCT;

import entity.CustomTag;

/**
 * The output data of the custom tag.
 */
public class CreateCustomTagOutputData {

    private final boolean useCaseFailed;
    private final CreateCustomTagError errorType;
    private final String errorMessage;
    private final CustomTag createdTag;

    // failed UseCase constructor
    public CreateCustomTagOutputData(CreateCustomTagError errorType) {
        this.useCaseFailed = true;
        this.errorType = errorType;
        this.errorMessage = errorType.getMessage();
        this.createdTag = null;
    }

    // successful UseCase constructor
    public CreateCustomTagOutputData(CustomTag createdTag) {
        useCaseFailed = false;
        errorType = null;
        errorMessage = null;
        this.createdTag = createdTag;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }

    public CustomTag getCreatedTag() {
        return createdTag;
    }
}
