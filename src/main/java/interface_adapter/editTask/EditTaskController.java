package interface_adapter.editTask;

import entity.Task;

import interface_adapter.ViewManagerModel;

import use_case.editTask.EditTaskInputBoundary;
import use_case.editTask.EditTaskInputData;

/**
 * Controller for editing a task, handling user interaction and communicating with the interactor.
 */
public class EditTaskController {

    private final EditTaskInputBoundary editTaskInputInteractor;
    private String username;
    private Task currentTask;
    private final ViewManagerModel viewManagerModel;

    /**
     * Constructs an EditTaskController with the given interactor and view manager.
     *
     * @param editTaskInputInteractor the input boundary interactor for editing tasks
     * @param viewManagerModel        the view manager model to switch views
     */
    public EditTaskController(EditTaskInputBoundary editTaskInputInteractor,
                              ViewManagerModel viewManagerModel) {
        this.editTaskInputInteractor = editTaskInputInteractor;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Sets the current username.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Executes editing the provided updated task.
     *
     * @param updatedTask the task with updated details
     */
    public void editTask(Task updatedTask) {
        System.out.println("[EditTaskController] Username is: " + username);
        final EditTaskInputData inputData = new EditTaskInputData(username, updatedTask);
        System.out.println("Loaded existing task with ID: " + updatedTask.getTaskInfo().getId());
        editTaskInputInteractor.execute(username, inputData);
    }

    /**
     * Switches the current view to the edit task view.
     *
     * @param managerModel the view manager model to change state
     */
    public void switchToEditTaskView(ViewManagerModel managerModel) {
        managerModel.setState("Edit Task");
        managerModel.firePropertyChanged("state");
    }

    /**
     * Returns the currently selected task.
     *
     * @return the current task
     */
    public Task getCurrentTask() {
        return currentTask;
    }

    /**
     * Sets the current task.
     *
     * @param currentTask the task to set as current
     */
    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    /**
     * Switches the view to create a custom tag.
     */
    public void createCustomTag() {
        viewManagerModel.setState("createCustomTag");
    }
}
