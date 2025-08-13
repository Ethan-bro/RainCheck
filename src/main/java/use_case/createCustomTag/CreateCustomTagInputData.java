package use_case.createCustomTag;

/**
 * The input data required for creating a custom tag.
 */
public class CreateCustomTagInputData {

    private String tagName;
    private String tagEmoji;

    /**
     * Constructs a CreateCustomTagInputData object with the provided tag name and emoji.
     *
     * @param tagName the name of the custom tag
     * @param tagEmoji the emoji/icon for the custom tag
     */
    public CreateCustomTagInputData(String tagName, String tagEmoji) {
        this.tagName = tagName;
        this.tagEmoji = tagEmoji;
    }

    /**
     * Returns the name of the custom tag.
     *
     * @return the tag name
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * Returns the emoji/icon for the custom tag.
     *
     * @return the tag emoji
     */
    public String getTagEmoji() {
        return tagEmoji;
    }

    /**
     * Sets the emoji/icon for the custom tag.
     *
     * @param tagEmoji the new tag emoji
     */
    public void setTagEmoji(String tagEmoji) {
        this.tagEmoji = tagEmoji;
    }

    /**
     * Sets the name of the custom tag.
     *
     * @param tagName the new tag name
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
