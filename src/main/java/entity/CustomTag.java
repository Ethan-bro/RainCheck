package entity;

public class CustomTag {

    private String tagName;
    private String tagEmoji;

    public CustomTag(String tagName, String tagEmoji) {
        this.tagName = tagName;
        this.tagEmoji = tagEmoji;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagEmoji() {
        return tagEmoji;
    }

    public void setTagEmoji(String tagEmoji) {
        this.tagEmoji = tagEmoji;
    }
}
