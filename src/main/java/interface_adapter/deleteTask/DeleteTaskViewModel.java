package interface_adapter.deleteTask;

import interface_adapter.ViewModel;

public class DeleteTaskViewModel extends ViewModel<DeleteTaskState> {

    public DeleteTaskViewModel() {
        super("Delete Task State");
        setState(new DeleteTaskState());
    }
}
