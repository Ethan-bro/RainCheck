package use_case.createCustomTag;

public interface createCustomTagOutputBoundary {
    void prepareFailView(createCustomTagOutputData failedOutputData);
    void prepareSuccessView(createCustomTagOutputData createCustomTagOutputData);
}
