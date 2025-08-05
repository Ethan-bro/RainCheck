package interface_adapter.deleteTask;

import entity.TaskID;
import use_case.deleteTask.DeleteTaskInputBoundary;
import use_case.deleteTask.DeleteTaskInputData;

import java.io.IOException;

public class DeleteTaskController {
    private final DeleteTaskInputBoundary deleteTaskInputInteractor;
    private String username;

    public DeleteTaskController(DeleteTaskInputBoundary deleteTaskInputInteractor) {
        this.deleteTaskInputInteractor = deleteTaskInputInteractor;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void deleteTask(TaskID taskId) throws IOException {
        DeleteTaskInputData inputData = new DeleteTaskInputData(username, taskId);
        deleteTaskInputInteractor.execute(inputData);
    }
}
