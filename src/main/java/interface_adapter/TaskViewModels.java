package interface_adapter;

import interface_adapter.deleteTask.DeleteTaskViewModel;
import interface_adapter.editTask.EditTaskViewModel;
import interface_adapter.markTaskComplete.MarkTaskCompleteViewModel;

public class TaskViewModels {
    private final MarkTaskCompleteViewModel markTaskCompleteViewModel;
    private final DeleteTaskViewModel deleteTaskViewModel;
    private final EditTaskViewModel editTaskViewModel;

    public TaskViewModels(MarkTaskCompleteViewModel markTaskCompleteViewModel,
                          DeleteTaskViewModel deleteTaskViewModel,
                          EditTaskViewModel editTaskViewModel) {
        this.markTaskCompleteViewModel = markTaskCompleteViewModel;
        this.deleteTaskViewModel = deleteTaskViewModel;
        this.editTaskViewModel = editTaskViewModel;
    }

    // Getters
    public MarkTaskCompleteViewModel getMarkTaskCompleteViewModel() {
        return markTaskCompleteViewModel;
    }

    public DeleteTaskViewModel getDeleteTaskViewModel() {
        return deleteTaskViewModel;
    }

    public EditTaskViewModel getEditTaskViewModel() {
        return editTaskViewModel;
    }
}