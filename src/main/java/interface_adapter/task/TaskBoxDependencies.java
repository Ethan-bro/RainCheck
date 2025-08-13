package interface_adapter.task;

import interface_adapter.ViewManagerModel;
import interface_adapter.deleteTask.DeleteTaskViewModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskViewModel;
import interface_adapter.markTaskComplete.MarkTaskCompleteViewModel;

/**
 * Holds dependencies required by a TaskBox, including view models and controllers.
 *
 * @param markTaskCompleteViewModel the view model for marking tasks complete
 * @param deleteTaskViewModel the view model for deleting tasks
 * @param viewManagerModel the view manager model
 * @param editTaskController the controller for editing tasks
 * @param editTaskViewModel the view model for editing tasks
 */
public record TaskBoxDependencies(
        MarkTaskCompleteViewModel markTaskCompleteViewModel,
        DeleteTaskViewModel deleteTaskViewModel,
        ViewManagerModel viewManagerModel,
        EditTaskController editTaskController,
        EditTaskViewModel editTaskViewModel
) {

}
