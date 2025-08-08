package use_case.EditCT;

import entity.CustomTag;

public class EditTagOutputData {

    private final boolean isUseCaseFailed;
    private final String errorMessage;
    private final CustomTag createdTag;

    // failed UseCase constructor
    public EditTagOutputData(String errorMessage) {
        this.isUseCaseFailed = true;
        this.errorMessage = errorMessage;
        this.createdTag = null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // successful UseCase constructor
    public EditTagOutputData(CustomTag createdTag) {
        this.isUseCaseFailed = false;
        this.errorMessage = null;
        this.createdTag = createdTag;
    }

    public boolean isUseCaseFailed() {
        return isUseCaseFailed;
    }

    public CustomTag getCreatedTag() {
        return createdTag;
    }
}
