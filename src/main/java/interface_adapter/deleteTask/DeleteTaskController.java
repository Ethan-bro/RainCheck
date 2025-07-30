package interface_adapter.deleteTask;

import entity.TaskID;
import use_case.deleteTask.DeleteTaskInputBoundary;
import use_case.deleteTask.DeleteTaskInputData;

public class DeleteTaskController {
    private final DeleteTaskInputBoundary deleteTaskInputInteractor;
    private final String username;

    public DeleteTaskController(String username, DeleteTaskInputBoundary deleteTaskInputInteractor) {
        this.deleteTaskInputInteractor = deleteTaskInputInteractor;
        this.username = username;
    }

    public void deleteTask(TaskID taskId) {
        DeleteTaskInputData inputData = new DeleteTaskInputData(username, taskId);
        deleteTaskInputInteractor.execute(username, inputData);
    }
}
