package interface_adapter.deleteTask;

import entity.TaskID;
import use_case.deleteTask.DeleteTaskInputBoundary;
import use_case.deleteTask.DeleteTaskInputData;

public class DeleteTaskController {
    private final DeleteTaskInputBoundary deleteTaskInputInteractor;
    private final String username;

    public DeleteTaskController(DeleteTaskInputBoundary deleteTaskInputInteractor, String username) {
        this.deleteTaskInputInteractor = deleteTaskInputInteractor;
        this.username = username;
    }

    public void deleteTask(TaskID taskId) {
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId);
        deleteTaskInputInteractor.execute(username, inputData);
    }
}
