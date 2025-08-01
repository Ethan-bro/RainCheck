package interface_adapter.task;

import interface_adapter.ViewManagerModel;
import interface_adapter.markTaskComplete.MarkTaskCompleteController;
import interface_adapter.markTaskComplete.MarkTaskCompleteViewModel;
import interface_adapter.deleteTask.DeleteTaskController;
import interface_adapter.editTask.EditTaskController;

public class TaskBoxDependencies {
    public final TaskViewModel taskViewModel;
    public final MarkTaskCompleteViewModel markTaskCompleteViewModel;
    public final ViewManagerModel viewManagerModel;
    public final MarkTaskCompleteController markTaskCompleteController;
    public final DeleteTaskController deleteTaskController;
    public final EditTaskController editTaskController;
    private String taskDependenciesUsername;

    public TaskBoxDependencies(TaskViewModel taskViewModel,
                               MarkTaskCompleteViewModel markTaskCompleteViewModel,
                               ViewManagerModel viewManagerModel,
                               MarkTaskCompleteController markTaskCompleteController,
                               DeleteTaskController deleteTaskController,
                               EditTaskController editTaskController) {
        this.taskViewModel = taskViewModel;
        this.markTaskCompleteViewModel = markTaskCompleteViewModel;
        this.viewManagerModel = viewManagerModel;
        this.markTaskCompleteController = markTaskCompleteController;
        this.deleteTaskController = deleteTaskController;
        this.editTaskController = editTaskController;
    }

    public TaskViewModel getTaskViewModel() {
        return taskViewModel;
    }

    public void setTaskDependenciesUsername(String taskDependenciesUsername) {
        this.taskDependenciesUsername = taskDependenciesUsername;
    }

    public String getTaskDependenciesUsername() {
        return taskDependenciesUsername;
    }
}
