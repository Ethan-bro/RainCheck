package interface_adapter.createTag;

import interface_adapter.ViewModel;

public class cctViewModel extends ViewModel<cctState> {

    private String username;

    public cctViewModel() {
        super("Create Custom Tag");
        setState(new cctState());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
