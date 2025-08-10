package interface_adapter.CreateTag;

import interface_adapter.ViewModel;

public class CreateTagViewModel extends ViewModel<CreateTagState> {

    private String username;

    public CreateTagViewModel() {
        super("Create Custom Tag");
        setState(new CreateTagState());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
