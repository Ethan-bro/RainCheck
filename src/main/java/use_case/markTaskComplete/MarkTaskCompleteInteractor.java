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
        final Task task = dataAccess.getTaskById(username, inputData.getTaskId());

        if (task == null) {
            presenter.prepareFailView("Task not found.");
        }
        else {
            // Marking the task as complete
            task.getTaskInfo().setTaskStatus("Complete");

            // Update the task in the data source
            dataAccess.updateTask(inputData.getUsername(), task);

            // Return a success view
            final MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(
                    task.getTaskInfo().getId(),
                    false
            );
            presenter.prepareSuccessView(outputData);
        }
    }
}
