package use_case.EditCT;

import entity.CustomTag;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.createCustomTag.CCTError;
import use_case.createCustomTag.CCTOutputData;
import use_case.createCustomTag.CustomTagDataAccessInterface;

import java.util.Map;

import static use_case.EditCT.TagErrorConstants.ICON_TAKEN_ERROR;
import static use_case.EditCT.TagErrorConstants.NAME_TAKEN_ERROR;

public class EditTagInteractor implements EditTagInputBoundary {

    private final CustomTagDataAccessInterface tagDao;
    private final EditTagOutputBoundary editTagPresenter;

    public EditTagInteractor(
            CustomTagDataAccessInterface tagDao,
            EditTagOutputBoundary editTagPresenter) {

        this.tagDao = tagDao;
        this.editTagPresenter = editTagPresenter;
    }

    public void execute(EditTagInputData inputData) {

        // check if any of the fields are blank (assume old and new tag are complete)
        // if (oldName == null || newName == null || newEmoji == null) return;

        // optional check:
        // if (oldName.equals(newName)) || oldEmoji.equals(newEmoji) return; // No change


        // old fields
        CustomTag oldTag = inputData.getOldTag();
        String oldName = inputData.getOldTagName();
        String oldIcon = inputData.getOldTagIcon();

        // new fields
        CustomTag newTag = inputData.getNewTag();
        String newName = inputData.getNewTagName();
        String newIcon = inputData.getNewTagIcon();

        // fetch user and tag data
        String username = inputData.getUsername();
        Map<String, String> existingTags = tagDao.getCustomTags(username);

        // delete the old tag
        tagDao.deleteCustomTag(username, oldTag);

        // Check if new tag name is already taken
        if (existingTags.containsKey(newName)) {
            EditTagOutputData failedOutput = new EditTagOutputData(NAME_TAKEN_ERROR);
            editTagPresenter.prepareFailView(failedOutput);
            return;
        }

        // Check if emoji is already in use
        if (existingTags.containsValue(newIcon)) {
            EditTagOutputData failedOutput = new EditTagOutputData(ICON_TAKEN_ERROR);
            editTagPresenter.prepareFailView(failedOutput);
            return;
        }

        // create the new tag
        tagDao.addCustomTag(username, newTag);

        EditTagOutputData successOutput = new EditTagOutputData(newTag);
        editTagPresenter.prepareSuccessView(successOutput);
    }
}
