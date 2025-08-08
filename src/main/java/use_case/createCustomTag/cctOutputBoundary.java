package use_case.createCustomTag;

public interface CCTOutputBoundary {
    void prepareFailView(CCTOutputData failedOutputData);
    void prepareSuccessView(CCTOutputData createCustomTagOutputData);
}
