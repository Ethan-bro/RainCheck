package interface_adapter.create_customTag;

import interface_adapter.logged_in.LoggedInState;

import javax.swing.*;

public class createCustomTagState {
    private String currTagName = "Custom Tag";
    private ImageIcon currTagIcon = null;
    private String ErrorMsg = "";

    public createCustomTagState(createCustomTagState copy) {
        currTagName = copy.currTagName;
        currTagIcon = copy.currTagIcon;
        ErrorMsg = copy.ErrorMsg;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public createCustomTagState() {

    }

    public String getCurrTagName() {
        return currTagName;
    }

    public void setCurrTagName(String newCurrTagName) {
        this.currTagName = newCurrTagName;
    }

    public void setCurrTagIcon(ImageIcon newCurrTagIcon) {
        this.currTagIcon = newCurrTagIcon;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String newErrorMsg) {
        ErrorMsg = newErrorMsg;
    }
}
