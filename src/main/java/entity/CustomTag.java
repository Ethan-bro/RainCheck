package entity;

public class CustomTag {

    private String tagName;
    private String tagIcon;

    /**
     * Constructs a CustomTag with the given name and icon.
     * @param tagName the tag name
     * @param tagIcon the tag icon
     */
    public CustomTag(String tagName, String tagIcon) {
        this.tagName = tagName;
        this.tagIcon = tagIcon;
    }

    /**
     * Gets the tag name.
     * @return the tag name
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * Sets the tag name.
     * @param tagName the tag name to set
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Gets the tag icon.
     * @return the tag icon
     */
    public String getTagIcon() {
        return tagIcon;
    }

    /**
     * Returns a string representation of the custom tag.
     * @return the tag icon and name as a string
     */
    @Override
    public String toString() {
        return tagIcon + "  " + tagName;
    }
}
