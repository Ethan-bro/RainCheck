package interface_adapter.editCustomTag;

public class EditTagState {
    private String currTagName = "Custom Tag";
    private String currTagEmoji;
    private String errorMsg = "";

    /**
     * Copy constructor for EditTagState.
     *
     * @param copy the EditTagState to copy from
     */
    public EditTagState(EditTagState copy) {
        currTagName = copy.currTagName;
        currTagEmoji = copy.currTagEmoji;
        errorMsg = copy.errorMsg;
    }

    /**
     * Default constructor for EditTagState.
     */
    public EditTagState() {

    }

    /**
     * Returns the current tag name.
     *
     * @return the current tag name
     */
    public String getCurrTagName() {
        return currTagName;
    }

    /**
     * Sets the current tag name.
     *
     * @param newCurrTagName the new tag name to set
     */
    public void setCurrTagName(String newCurrTagName) {
        this.currTagName = newCurrTagName;
    }

    /**
     * Sets the current tag emoji.
     *
     * @param newCurrTagEmoji the new tag emoji to set
     */
    public void setCurrTagEmoji(String newCurrTagEmoji) {
        this.currTagEmoji = newCurrTagEmoji;
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
}
