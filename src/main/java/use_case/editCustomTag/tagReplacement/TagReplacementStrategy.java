package use_case.editCustomTag.tagReplacement;

import entity.CustomTag;

import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.editCustomTag.EditTagInputData;

public interface TagReplacementStrategy {

    /**
     * Replaces an old custom tag with a new one for the specified user.
     *
     * @param inputData the input data containing old and new tags and username
     * @param tagDao the data access interface for custom tags
     * @return true if replacement was successful, false otherwise
     */
    Boolean replaceTag(EditTagInputData inputData, CustomTagDataAccessInterface tagDao);

    /**
     * Gets the status message after attempting tag replacement.
     *
     * @return the status message (error or success)
     */
    String getStatusMsg();

    /**
     * Gets the newly created custom tag after replacement.
     *
     * @return the created custom tag, or null if replacement failed
     */
    CustomTag getCreatedTag();
}
