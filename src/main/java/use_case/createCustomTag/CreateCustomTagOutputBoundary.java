package use_case.createCustomTag;

public interface cctOutputBoundary {
    void prepareFailView(CreateCustomTagOutputData failedOutputData);
    void prepareSuccessView(CreateCustomTagOutputData createCustomTagOutputData);
}
