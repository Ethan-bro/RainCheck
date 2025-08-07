package entity;

public class CustomTag {

    private String tagName;
    private String tagIcon;

    public CustomTag(String tagName, String tagIcon) {
        this.tagName = tagName;
        this.tagIcon = tagIcon;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagIcon() {
        return tagIcon;
    }

    public void setTagIcon(String tagEmoji) {
        this.tagIcon = tagEmoji;
    }

    @Override
    public String toString() {
        return tagIcon + "  " + tagName;
    }
}
