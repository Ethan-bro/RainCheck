package use_case.edit_custom_tag;

import entity.CustomTag;

import use_case.createCustomTag.CustomTagDataAccessInterface;

import java.util.Map;

/**
 * Interactor class implementing the EditTagInputBoundary.
 */
public class EditTagInteractor implements EditTagInputBoundary {

    private final CustomTagDataAccessInterface tagDao;
    private final EditTagOutputBoundary editTagPresenter;

    public EditTagInteractor(
            CustomTagDataAccessInterface tagDao,
            EditTagOutputBoundary editTagPresenter) {
        this.tagDao = tagDao;
        this.editTagPresenter = editTagPresenter;
    }

    /**
     * Executes the tag editing use case.
     *
     * @param inputData the input data containing old and new tag information
     */
    @Override
    public void execute(final EditTagInputData inputData) {
        final CustomTag oldTag = inputData.getOldTag();
        final CustomTag newTag = inputData.getNewTag();
        final String newName = inputData.getNewTagName();
        final String newIcon = inputData.getNewTagIcon();
        final String username = inputData.getUsername();

        final Map<String, String> existingTags = tagDao.getCustomTags(username);

        // Ignore the old tag when checking for duplicates so that editing a tag
        // to keep the same icon or same name does not trigger a "taken" error
        final boolean isNameTaken = existingTags.containsKey(newName) && !oldTag.getTagName().equals(newName);
        final boolean isIconTaken = existingTags.containsValue(newIcon) && !oldTag.getTagIcon().equals(newIcon);

        if (isNameTaken) {
            final EditTagOutputData failedOutput =
                    new EditTagOutputData(TagErrorConstants.NAME_TAKEN_ERROR);
            editTagPresenter.prepareFailView(failedOutput);
        }
        else if (isIconTaken) {
            final EditTagOutputData failedOutput =
                    new EditTagOutputData(TagErrorConstants.ICON_TAKEN_ERROR);
            editTagPresenter.prepareFailView(failedOutput);
        }
        else {
            tagDao.deleteCustomTag(username, oldTag);
            tagDao.addCustomTag(username, newTag);
            final EditTagOutputData successOutput = new EditTagOutputData(newTag);
            editTagPresenter.prepareSuccessView(successOutput);
        }
    }
}
