package interface_adapter.editTask;

import interface_adapter.ViewModel;

public class EditTaskViewModel extends ViewModel<EditTaskState> {

    public EditTaskViewModel(String viewName) {
        super(viewName);
        setState(new EditTaskState());
    }

    @Override
    public void setState(EditTaskState state) {
        super.setState(state);
        firePropertyChanged("state");
    }
}
