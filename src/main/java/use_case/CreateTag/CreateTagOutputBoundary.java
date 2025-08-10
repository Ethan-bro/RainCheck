package use_case.CreateTag;

public interface CreateTagOutputBoundary {
    void prepareFailView(CreateTagOutputData failedOutputData);
    void prepareSuccessView(CreateTagOutputData createCustomTagOutputData);
}
