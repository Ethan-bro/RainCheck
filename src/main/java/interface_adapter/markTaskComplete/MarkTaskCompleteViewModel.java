package interface_adapter.markTaskComplete;

import interface_adapter.ViewModel;

public class MarkTaskCompleteViewModel extends ViewModel<MarkTaskCompleteState> {

    /**
     * Constructs a MarkTaskCompleteViewModel and initializes its state.
     */
    public MarkTaskCompleteViewModel() {
        super("Mark Task Complete State");
        setState(new MarkTaskCompleteState());
    }
}
