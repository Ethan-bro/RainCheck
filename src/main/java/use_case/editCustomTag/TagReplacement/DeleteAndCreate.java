package use_case.editCustomTag.TagReplacement;

import java.util.Map;

import entity.CustomTag;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.editCustomTag.EditTagInputData;
import static use_case.editCustomTag.TagErrorConstants.ICON_TAKEN_ERROR;
import static use_case.editCustomTag.TagErrorConstants.NAME_TAKEN_ERROR;

public class DeleteAndCreate implements TagReplacementStrategy {

    private String statusMsg;
    private CustomTag createdTag;

    /**
     * Constructs a DeleteAndCreate strategy for tag replacement.
     */
    public DeleteAndCreate() {
    }

    /**
     * Replaces an old custom tag with a new one for the specified user.
     * Deletes the old tag, checks for name/icon conflicts, and creates the new tag if valid.
     *
     * @param inputData the input data containing old and new tags and username
     * @param tagDao the data access interface for custom tags
     * @return true if replacement was successful, false otherwise
     */
    @Override
    public Boolean replaceTag(EditTagInputData inputData, CustomTagDataAccessInterface tagDao) {
        CustomTag oldTag = inputData.getOldTag();
        CustomTag newTag = inputData.getNewTag();
        String username = inputData.getUsername();

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

    /**
     * Gets the status message after attempting tag replacement.
     *
     * @return the status message (error or success)
     */
    @Override
    public String getStatusMsg() {
        return statusMsg;
    }

    /**
     * Gets the newly created custom tag after replacement.
     *
     * @return the created custom tag, or null if replacement failed
     */
    @Override
    public CustomTag getCreatedTag() {
        return createdTag;
    }
}
