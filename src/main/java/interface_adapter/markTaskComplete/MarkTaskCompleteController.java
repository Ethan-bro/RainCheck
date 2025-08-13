package interface_adapter.markTaskComplete;

import entity.TaskID;

import use_case.markTaskComplete.MarkTaskCompleteInputBoundary;
import use_case.markTaskComplete.MarkTaskCompleteInputData;

/**
 * Controller to mark a task as complete.
 */
public class MarkTaskCompleteController {

    private final MarkTaskCompleteInputBoundary markTaskCompleteInteractor;
    private String username;

    /**
     * Constructs a MarkTaskCompleteController with the given interactor.
     *
     * @param markTaskCompleteInteractor the interactor for marking tasks complete
     */
    public MarkTaskCompleteController(MarkTaskCompleteInputBoundary markTaskCompleteInteractor) {
        this.markTaskCompleteInteractor = markTaskCompleteInteractor;
    }

    /**
     * Sets the username for the user performing the operation.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Marks the task identified by taskId as complete if username is set.
     *
     * @param taskId the ID of the task to mark as complete
     */
    public void markAsComplete(TaskID taskId) {
        if (username != null) {
            final MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(username, taskId);
            markTaskCompleteInteractor.execute(username, inputData);
        }
        else {
            System.out.println("-------------- username is null --------------");
        }
    }
}
