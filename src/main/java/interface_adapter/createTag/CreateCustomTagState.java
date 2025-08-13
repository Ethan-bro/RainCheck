package interface_adapter.createTag;

public class CreateCustomTagState {
    private String currTagName = "Custom Tag";
    private String currTagEmoji;
    private String errorMsg = "";

    /**
     * Copy constructor for CreateCustomTagState.
     *
     * @param copy the state to copy from
     */
    public CreateCustomTagState(CreateCustomTagState copy) {
        currTagName = copy.currTagName;
        currTagEmoji = copy.currTagEmoji;
        errorMsg = copy.errorMsg;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    /**
     * Default constructor for CreateCustomTagState.
     */
    public CreateCustomTagState() {
    }

    /**
     * Gets the current tag name.
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
     * Gets the error message for the custom tag operation.
     *
     * @return the error message
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Sets the error message for the custom tag operation.
     *
     * @param newErrorMsg the error message to set
     */
    public void setErrorMsg(String newErrorMsg) {
        errorMsg = newErrorMsg;
    }
}
