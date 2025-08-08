package use_case.CreateCT;

public interface CCTOutputBoundary {
    void prepareFailView(CCTOutputData failedOutputData);
    void prepareSuccessView(CCTOutputData createCustomTagOutputData);
}
