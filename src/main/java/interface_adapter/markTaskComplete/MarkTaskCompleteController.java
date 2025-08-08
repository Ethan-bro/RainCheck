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

    public MarkTaskCompleteController(MarkTaskCompleteInputBoundary markTaskCompleteInteractor) {
        this.markTaskCompleteInteractor = markTaskCompleteInteractor;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Marks the task identified by taskId as complete if username is set.
     *
     * @param taskId the ID of the task to mark complete
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
