package use_case.addTask;

public interface AddTaskOutputBoundary {
    void prepareFailView(AddTaskOutputData failedOutputData);
    void prepareSuccessView(AddTaskOutputData successOutputData);
}
