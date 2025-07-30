package use_case.markTaskComplete;

import entity.Task;

public class MarkTaskCompleteInteractor implements MarkTaskCompleteInputBoundary {

    private final MarkTaskCompleteDataAccessInterface dataAccess;
    private final MarkTaskCompleteOutputBoundary presenter;

    public MarkTaskCompleteInteractor(MarkTaskCompleteDataAccessInterface dataAccess,
                                      MarkTaskCompleteOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(String username, MarkTaskCompleteInputData inputData) {
        // Retrieving the task
        Task task = dataAccess.getTaskById(username, inputData.getTaskId());

        if (task == null) {
            presenter.prepareFailView("Task not found.");
            return;
        }

        // Marking the task as complete
        dataAccess.markAsComplete(username, inputData.getTaskId());

        // Updating the task in the database
        dataAccess.updateTask(username, task);

        // Returning a success view
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(
                task.getTaskInfo().getId(),
                false  // useCaseFailed = false (meaning success)
        );
        presenter.prepareSuccessView(outputData);
    }
}
