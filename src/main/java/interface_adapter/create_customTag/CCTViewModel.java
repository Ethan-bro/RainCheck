package interface_adapter.create_customTag;

import interface_adapter.ViewModel;

public class CCTViewModel extends ViewModel<CCTState> {

    private String username;

    public CCTViewModel() {
        super("Create Custom Tag");
        setState(new CCTState());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
