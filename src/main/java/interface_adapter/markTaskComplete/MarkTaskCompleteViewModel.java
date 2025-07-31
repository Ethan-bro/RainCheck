package interface_adapter.markTaskComplete;

import interface_adapter.ViewModel;


public class MarkTaskCompleteViewModel extends ViewModel<MarkTaskCompleteState> {

    public MarkTaskCompleteViewModel(String viewName) {
        super("Mark Task Complete State");
        setState(new MarkTaskCompleteState());
    }
}


