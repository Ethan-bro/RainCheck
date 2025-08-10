package use_case.EditTag;

public interface EditTagOutputBoundary {

    public void prepareSuccessView(EditTagOutputData successOutput);

    public void prepareFailView(EditTagOutputData failedOutput);
}
