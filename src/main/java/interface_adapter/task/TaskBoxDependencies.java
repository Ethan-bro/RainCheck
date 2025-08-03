package interface_adapter.task;

import interface_adapter.ViewManagerModel;
import interface_adapter.deleteTask.DeleteTaskViewModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.markTaskComplete.MarkTaskCompleteViewModel;

public record TaskBoxDependencies(
        MarkTaskCompleteViewModel markTaskCompleteViewModel,
        DeleteTaskViewModel deleteTaskViewModel,
        ViewManagerModel viewManagerModel,
        EditTaskController editTaskController
) {}
