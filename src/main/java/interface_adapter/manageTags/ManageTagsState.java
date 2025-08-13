package interface_adapter.manageTags;

import entity.CustomTag;

public class ManageTagsState {

    private String currUsername;
    private CustomTag currTag;
    private String errorMsg;

    /**
     * Copy constructor for ManageTagsState.
     *
     * @param copy the ManageTagsState to copy from
     */
    public ManageTagsState(ManageTagsState copy) {
        this.currUsername = copy.currUsername;
        this.currTag = copy.currTag;
        errorMsg = copy.errorMsg;
    }

    /**
     * Default constructor for ManageTagsState.
     */
    public ManageTagsState() {

    }

    /**
     * Returns the current username.
     *
     * @return the current username
     */
    public String getCurrUsername() {
        return currUsername;
    }

    /**
     * Returns the current tag.
     *
     * @return the current CustomTag
     */
    public CustomTag getCurrTag() {
        return currTag;
    }

    /**
     * Returns the error message, if any.
     *
     * @return the error message
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Sets the error message.
     *
     * @param newErrorMsg the error message to set
     */
    public void setErrorMsg(String newErrorMsg) {
        errorMsg = newErrorMsg;
    }

    /**
     * Sets the current tag.
     *
     * @param selectedTag the CustomTag to set as current
     */
    public void setCurrTag(CustomTag selectedTag) {
        currTag = selectedTag;
    }
}
