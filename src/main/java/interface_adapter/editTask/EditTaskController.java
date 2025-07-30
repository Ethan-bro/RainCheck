package interface_adapter.editTask;

import entity.Task;
import use_case.editTask.EditTaskInputBoundary;
import use_case.editTask.EditTaskInputData;

public class EditTaskController {

    private final EditTaskInputBoundary editTaskInputInteractor;
    private final String username;

    public EditTaskController(EditTaskInputBoundary editTaskInputInteractor, String username) {
        this.editTaskInputInteractor = editTaskInputInteractor;
        this.username = username;
    }

    public void editTask(Task updatedTask) {
        EditTaskInputData inputData = new EditTaskInputData(username, updatedTask);
            editTaskInputInteractor.execute(username, inputData);
    }
}

