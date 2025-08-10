package interface_adapter.CreateTag;

public class CreateTagState {
    private String currTagName = "Custom Tag";
    private String currTagEmoji;
    private String errorMsg = "";

    public CreateTagState(CreateTagState copy) {
        currTagName = copy.currTagName;
        currTagEmoji = copy.currTagEmoji;
        errorMsg = copy.errorMsg;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public CreateTagState() {

    }

    public String getCurrTagName() {
        return currTagName;
    }

    public void setCurrTagName(String newCurrTagName) {
        this.currTagName = newCurrTagName;
    }

    public void setCurrTagEmoji(String newCurrTagEmoji) {
        this.currTagEmoji = newCurrTagEmoji;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String newErrorMsg) {
        errorMsg = newErrorMsg;
    }
}
