package interface_adapter.editTag;

import entity.CustomTag;
import interface_adapter.ViewModel;

public class editTagViewModel extends ViewModel<editTagState> {

    private String username;

    public editTagViewModel() {
        super("Edit Custom Tag");
        setState(new editTagState());

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
