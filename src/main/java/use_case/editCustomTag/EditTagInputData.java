package use_case.editCustomTag;

import entity.CustomTag;

public class EditTagInputData {

    private CustomTag oldTag;
    private String newName;
    private String newIcon;
    private String username;

    /**
     * Constructs an EditTagInputData object.
     * @param oldTag the existing tag
     * @param newName the modified tag name
     * @param newIcon the modified tag icon
     * @param username the username associated with the tag edit
     */
    public EditTagInputData(CustomTag oldTag,
                            String newName,
                            String newIcon,
                            String username) {

        this.oldTag = oldTag;
        this.newName = newName;
        this.newIcon = newIcon;
        this.username = username;
    }

    public CustomTag getOldTag() {
        return oldTag;
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
        return newName;
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
        return newIcon;
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
