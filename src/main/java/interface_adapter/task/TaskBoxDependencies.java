package interface_adapter.task;

import interface_adapter.ViewManagerModel;
import interface_adapter.markTaskComplete.MarkTaskCompleteController;
import interface_adapter.markTaskComplete.MarkTaskCompleteViewModel;
import interface_adapter.deleteTask.DeleteTaskController;
import interface_adapter.editTask.EditTaskController;

public record TaskBoxDependencies(TaskViewModel taskViewModel, MarkTaskCompleteViewModel markTaskCompleteViewModel,
                                  ViewManagerModel viewManagerModel,
                                  MarkTaskCompleteController markTaskCompleteController,
                                  DeleteTaskController deleteTaskController, EditTaskController editTaskController) {
}
