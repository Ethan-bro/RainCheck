package interface_adapter.deleteTask;

import interface_adapter.ViewModel;

public class DeleteTaskViewModel extends ViewModel<DeleteTaskState> {

    /**
     * Constructs a DeleteTaskViewModel and initializes its state.
     */
    public DeleteTaskViewModel() {
        super("Delete Task State");
        setState(new DeleteTaskState());
    }
}
