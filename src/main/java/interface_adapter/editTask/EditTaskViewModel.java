package interface_adapter.editTask;

import interface_adapter.ViewModel;

public class EditTaskViewModel extends ViewModel<EditTaskState> {

    public EditTaskViewModel(String viewName) {
        super("Edit Task State");
        setState(new EditTaskState());

    }
}
