package use_case.editCustomTag.tagReplacement;

import entity.CustomTag;

import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.editCustomTag.EditTagInputData;
import use_case.editCustomTag.TagErrorConstants;

import java.util.Map;

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

        final String username = inputData.getUsername();
        final CustomTag oldTag = inputData.getOldTag();

        // new fields
        final String newName = inputData.getNewTagName();
        final String newIcon = inputData.getNewTagIcon();

        // delete the old tag
        tagDao.deleteCustomTag(username, oldTag);

        // fetch tag data
        final Map<String, String> existingTags = tagDao.getCustomTags(username);

        final boolean success;

        // check if new tag name is already taken
        if (existingTags.containsKey(newName)) {
            statusMsg = TagErrorConstants.getNameTakenError();
            success = false;
        }
        else if (existingTags.containsValue(newIcon)) {
            // check if emoji is already in use
            statusMsg = TagErrorConstants.getIconTakenError();
            success = false;
        }
        else {
            // otherwise, create the new tag
            CustomTag newTag = new CustomTag(newName, newIcon);
            tagDao.addCustomTag(username, newTag);
            createdTag = newTag;

            // indicate that replacement process was a success
            success = true;
        }

        return success;
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
