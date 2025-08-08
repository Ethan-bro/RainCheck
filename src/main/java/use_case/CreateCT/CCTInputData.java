package use_case.CreateCT;

/**
 * The input data required for creating a custom tag.
 */
public class CCTInputData {

    private String tagName;
    private String tagEmoji;

    public CCTInputData(String tagName, String tagEmoji) {
        this.tagName = tagName;
        this.tagEmoji = tagEmoji;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTagEmoji() {
        return tagEmoji;
    }

    public void setTagEmoji(String tagEmoji) {
        this.tagEmoji = tagEmoji;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
