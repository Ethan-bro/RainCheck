package use_case.edit_custom_tag.tagReplacement;

import entity.CustomTag;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.edit_custom_tag.EditTagInputData;

import java.util.Map;

import static use_case.edit_custom_tag.TagErrorConstants.ICON_TAKEN_ERROR;
import static use_case.edit_custom_tag.TagErrorConstants.NAME_TAKEN_ERROR;

public class DeleteAndCreate implements TagReplacementStrategy {

    private String statusMsg;
    private CustomTag createdTag;

    public DeleteAndCreate() {
    }

    @Override
    public Boolean replaceTag(EditTagInputData inputData, CustomTagDataAccessInterface tagDao) {

        CustomTag oldTag = inputData.getOldTag();
        CustomTag newTag = inputData.getNewTag();
        String username = inputData.getUsername();

        // old fields
        String oldName = oldTag.getTagName();
        String oldIcon = oldTag.getTagIcon();

        // new fields
        String newName = newTag.getTagName();
        String newIcon = newTag.getTagIcon();

        // delete the old tag
        tagDao.deleteCustomTag(username, oldTag);

        // fetch tag data
        Map<String, String> existingTags = tagDao.getCustomTags(username);

        // check if new tag name is already taken
        if (existingTags.containsKey(newName)) {
            statusMsg = NAME_TAKEN_ERROR;
            return false;
        }

        // check if emoji is already in use
        if (existingTags.containsValue(newIcon)) {
            statusMsg = ICON_TAKEN_ERROR;
            return false;
        }

        // create the new tag
        tagDao.addCustomTag(username, newTag);
        createdTag = newTag;

        // indicate that replacement process was a success
        return true;
    }

    @Override
    public String getStatusMsg() {
        return statusMsg;
    }

    @Override
    public CustomTag getCreatedTag() {
        return createdTag;
    }
}
