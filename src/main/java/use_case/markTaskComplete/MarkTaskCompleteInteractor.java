package use_case.markTaskComplete;

import entity.Task;

public class MarkTaskCompleteInteractor implements MarkTaskCompleteInputBoundary {

    private final MarkTaskCompleteDataAccessInterface dataAccess;
    private final MarkTaskCompleteOutputBoundary presenter;

    /**
     * Constructs a MarkTaskCompleteInteractor.
     *
     * @param dataAccess the data access interface for tasks
     * @param presenter the output boundary to present results
     */
    public MarkTaskCompleteInteractor(MarkTaskCompleteDataAccessInterface dataAccess,
                                      MarkTaskCompleteOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    /**
     * Executes the use case for marking a task as complete.
     *
     * @param username the username of the user marking the task complete
     * @param inputData the input data containing task ID
     */
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
