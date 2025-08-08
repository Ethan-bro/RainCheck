package interface_adapter.markTaskComplete;

import entity.TaskID;
import use_case.markTaskComplete.MarkTaskCompleteInputBoundary;
import use_case.markTaskComplete.MarkTaskCompleteInputData;

/**
 * Controller for marking tasks as complete.
 */
public class MarkTaskCompleteController {

    private final MarkTaskCompleteInputBoundary markTaskCompleteInteractor;
    private String username;

    public MarkTaskCompleteController(MarkTaskCompleteInputBoundary markTaskCompleteInteractor) {
        this.markTaskCompleteInteractor = markTaskCompleteInteractor;
        this.username = null;
    }

    /**
     * Sets the username for the current session.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Marks the specified task as complete.
     * @param taskId the ID of the task to mark complete
     */
    public void markAsComplete(TaskID taskId) {

        if (username == null) {
            System.out.println("-------------- username is null --------------");
        }

        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(username, taskId);
        markTaskCompleteInteractor.execute(username, inputData);
    }
}
