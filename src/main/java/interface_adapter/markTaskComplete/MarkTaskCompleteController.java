package interface_adapter.markTaskComplete;

import entity.TaskID;
import use_case.markTaskComplete.MarkTaskCompleteInputBoundary;
import use_case.markTaskComplete.MarkTaskCompleteInputData;

public class MarkTaskCompleteController {

    private final MarkTaskCompleteInputBoundary markTaskCompleteInteractor;
    private String username = null;

    public MarkTaskCompleteController(MarkTaskCompleteInputBoundary markTaskCompleteInteractor) {
        this.markTaskCompleteInteractor = markTaskCompleteInteractor;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void markAsComplete(TaskID taskId) {

        if (username == null) {
            System.out.println("-------------- username is null --------------");
            return;
        }

        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(username, taskId);
        markTaskCompleteInteractor.execute(username, inputData);
    }
}
