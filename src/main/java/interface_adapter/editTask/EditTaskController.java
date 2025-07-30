package interface_adapter.editTask;

import entity.Task;
import use_case.editTask.EditTaskInputBoundary;
import use_case.editTask.EditTaskInputData;
import interface_adapter.ViewManagerModel;
public class EditTaskController {

    private final EditTaskInputBoundary editTaskInputInteractor;
    private final String username;
    private Task currentTask;

    public EditTaskController(EditTaskInputBoundary editTaskInputInteractor, String username) {
        this.editTaskInputInteractor = editTaskInputInteractor;
        this.username = username;
    }

    public void editTask(Task updatedTask) {
        EditTaskInputData inputData = new EditTaskInputData(username, updatedTask);
        editTaskInputInteractor.execute(username, inputData);
    }

    public void switchToEditTaskView(ViewManagerModel viewManagerModel) {
        viewManagerModel.setState("Edit Task");
        viewManagerModel.firePropertyChanged("state");
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }
}
