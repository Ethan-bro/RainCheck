package use_case.edit_custom_tag;

import entity.CustomTag;

public class EditTagInputData {

    private CustomTag oldTag;
    private CustomTag newTag;
    private String username;

    public EditTagInputData(CustomTag oldTag, CustomTag newTag, String username) {
        this.oldTag = oldTag;
        this.newTag = newTag;
        this.username = username;
    }

    public CustomTag getOldTag() {
        return oldTag;
    }

    public void setOldTag(CustomTag oldTag) {
        this.oldTag = oldTag;
    }

    public CustomTag getNewTag() {
        return newTag;
    }

    public void setNewTag(CustomTag newTag) {
        this.newTag = newTag;
    }

    public String getOldTagName() {
        return oldTag.getTagName();
    }

    public String getNewTagName() {
        return newTag.getTagName();
    }

    public String getOldTagIcon() {
        return oldTag.getTagIcon();
    }

    public String getNewTagIcon() {
        return newTag.getTagIcon();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
