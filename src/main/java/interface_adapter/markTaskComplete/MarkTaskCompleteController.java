package interface_adapter.markTaskComplete;

import entity.TaskID;
import use_case.markTaskComplete.MarkTaskCompleteInputBoundary;
import use_case.markTaskComplete.MarkTaskCompleteInputData;

public class MarkTaskCompleteController {

    private final MarkTaskCompleteInputBoundary markTaskCompleteInteractor;
    private final String username;

    public MarkTaskCompleteController(MarkTaskCompleteInputBoundary markTaskCompleteInteractor, String username) {
        this.markTaskCompleteInteractor = markTaskCompleteInteractor;
        this.username = username;
    }

    public void markAsComplete(TaskID taskId) {
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId);
        markTaskCompleteInteractor.execute(username, inputData);
    }
}
