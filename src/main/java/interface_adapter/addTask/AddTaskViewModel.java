package interface_adapter.addTask;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;

public class AddTaskViewModel extends ViewModel<AddTaskState> {

    public AddTaskViewModel() {
        super("Add New Task");
        setState(new AddTaskState());
    }

}
