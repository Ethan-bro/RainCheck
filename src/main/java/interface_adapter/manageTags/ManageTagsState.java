package interface_adapter.manageTags;

import entity.CustomTag;

public class ManageTagsState {

    private String currUsername;
    private CustomTag currTag;
    private String errorMsg;

    public ManageTagsState(ManageTagsState copy) {
        this.currUsername = copy.currUsername;
        this.currTag = copy.currTag;
        errorMsg = copy.errorMsg;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public ManageTagsState() {

    }

    public String getCurrUsername() {
        return currUsername;
    }

    public CustomTag getCurrTag() {
        return currTag;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String newErrorMsg) {
        errorMsg = newErrorMsg;
    }

    public void setCurrTag(CustomTag selectedTag) {
        currTag = selectedTag;
    }
}
