package use_case.EditCT;

import entity.CustomTag;

public class EditTagOutputData {

    private final boolean UseCaseFailed;
    private final String errorMessage;
    private final CustomTag createdTag;

    // failed UseCase constructor
    public EditTagOutputData(String errorMessage) {
        this.UseCaseFailed = true;
        this.errorMessage = errorMessage;
        this.createdTag = null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // successful UseCase constructor
    public EditTagOutputData(CustomTag createdTag) {
        UseCaseFailed = false;
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
