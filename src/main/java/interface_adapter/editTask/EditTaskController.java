package interface_adapter.editTask;

import entity.Task;
import interface_adapter.ViewManagerModel;
import use_case.editTask.EditTaskInputBoundary;
import use_case.editTask.EditTaskInputData;

/**
 * Controller for editing tasks.
 */
public class EditTaskController {

    private final EditTaskInputBoundary editTaskInputInteractor;
    private String username;
    private Task currentTask;
    private final ViewManagerModel viewManagerModel;

    /**
     * Constructs the controller.
     * @param editTaskInputInteractor the interactor to handle task editing
     * @param viewManagerModel the view manager model for switching views
     */
    public EditTaskController(EditTaskInputBoundary editTaskInputInteractor, ViewManagerModel viewManagerModel) {
        this.editTaskInputInteractor = editTaskInputInteractor;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Sets the username for the current editing session.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Initiates editing of a task.
     * @param updatedTask the updated task data
     */
    public void editTask(Task updatedTask) {
        System.out.println("[EditTaskController] Username is: " + username);
        EditTaskInputData inputData = new EditTaskInputData(username, updatedTask);
        System.out.println("Loaded existing task with ID: " + updatedTask.getTaskInfo().getId());
        editTaskInputInteractor.execute(username, inputData);
    }

    /**
     * Switches the view to the edit task screen.
     * @param viewManagerModel the view manager model to update
     */
    public void switchToEditTaskView(ViewManagerModel viewManagerModel) {
        viewManagerModel.setState("Edit Task");
        viewManagerModel.firePropertyChanged("state");
    }

    /**
     * Returns the current task being edited.
     * @return the current task
     */
    public Task getCurrentTask() {
        return currentTask;
    }

    /**
     * Sets the current task to be edited.
     * @param currentTask the task to set
     */
    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    /**
     * Switches the view to the custom tag creation screen.
     */
    public void createCustomTag() {
        viewManagerModel.setState("createCustomTag");
    }
}
