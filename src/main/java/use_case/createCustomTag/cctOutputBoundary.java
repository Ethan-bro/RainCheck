package use_case.createCustomTag;

public interface cctOutputBoundary {
    void prepareFailView(cctOutputData failedOutputData);
    void prepareSuccessView(cctOutputData createCustomTagOutputData);
}
