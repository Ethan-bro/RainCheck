package interface_adapter.task;

import interface_adapter.ViewManagerModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.markTaskComplete.MarkTaskCompleteViewModel;

public record TaskBoxDependencies(
        MarkTaskCompleteViewModel markTaskCompleteViewModel,
        ViewManagerModel viewManagerModel,
        EditTaskController editTaskController
) {}
