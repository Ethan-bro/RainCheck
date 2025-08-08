package interface_adapter.createTag;

import interface_adapter.ViewModel;

public class CreateCustomTagViewModel extends ViewModel<CreateCustomTagState> {

    private String username;

    public CreateCustomTagViewModel() {
        super("Create Custom Tag");
        setState(new CreateCustomTagState());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
