package interface_adapter.EditTag;

import entity.CustomTag;
import interface_adapter.ViewModel;

public class EditTagViewModel extends ViewModel<EditTagState> {

    private String username;

    public EditTagViewModel() {
        super("Edit Custom Tag");
        setState(new EditTagState());

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void loadTag(CustomTag tag) {

        firePropertyChanged("LoadTag");
    }
}
