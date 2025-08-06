package interface_adapter.editTask;

import entity.Task;
import use_case.editTask.EditTaskInputBoundary;
import use_case.editTask.EditTaskInputData;
import interface_adapter.ViewManagerModel;
public class EditTaskController {

    private final EditTaskInputBoundary editTaskInputInteractor;
    private String username;
    private Task currentTask;
    private final ViewManagerModel viewManagerModel;


    public EditTaskController(EditTaskInputBoundary editTaskInputInteractor, ViewManagerModel viewManagerModel) {
        this.editTaskInputInteractor = editTaskInputInteractor;
        this.viewManagerModel = viewManagerModel;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void editTask(Task updatedTask) {
        System.out.println("[EditTaskController] Username is: " + username);
        EditTaskInputData inputData = new EditTaskInputData(username, updatedTask);
        System.out.println("Loaded existing task with ID: " + updatedTask.getTaskInfo().getId());
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

    public void createCustomTag() {
        viewManagerModel.setState("createCustomTag");
    }
}
