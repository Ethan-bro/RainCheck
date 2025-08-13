package use_case.editCustomTag;

import entity.CustomTag;

public class EditTagInputData {

    private CustomTag oldTag;
    private CustomTag newTag;
    private String username;

    /**
     * Constructs an EditTagInputData object.
     *
     * @param oldTag the original custom tag
     * @param newTag the new custom tag to replace the old one
     * @param username the username associated with the tag edit
     */
    public EditTagInputData(CustomTag oldTag, CustomTag newTag, String username) {
        this.oldTag = oldTag;
        this.newTag = newTag;
        this.username = username;
    }

    /**
     * Gets the original custom tag.
     *
     * @return the old custom tag
     */
    public CustomTag getOldTag() {
        return oldTag;
    }

    /**
     * Sets the original custom tag.
     *
     * @param oldTag the old custom tag
     */
    public void setOldTag(CustomTag oldTag) {
        this.oldTag = oldTag;
    }

    /**
     * Gets the new custom tag to replace the old one.
     *
     * @return the new custom tag
     */
    public CustomTag getNewTag() {
        return newTag;
    }

    /**
     * Sets the new custom tag to replace the old one.
     *
     * @param newTag the new custom tag
     */
    public void setNewTag(CustomTag newTag) {
        this.newTag = newTag;
    }

    /**
     * Gets the name of the original custom tag.
     *
     * @return the name of the old custom tag
     */
    public String getOldTagName() {
        return oldTag.getTagName();
    }

    /**
     * Gets the name of the new custom tag.
     *
     * @return the name of the new custom tag
     */
    public String getNewTagName() {
        return newTag.getTagName();
    }

    /**
     * Gets the icon of the original custom tag.
     *
     * @return the icon of the old custom tag
     */
    public String getOldTagIcon() {
        return oldTag.getTagIcon();
    }

    /**
     * Gets the icon of the new custom tag.
     *
     * @return the icon of the new custom tag
     */
    public String getNewTagIcon() {
        return newTag.getTagIcon();
    }

    /**
     * Gets the username associated with the tag edit.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username associated with the tag edit.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
