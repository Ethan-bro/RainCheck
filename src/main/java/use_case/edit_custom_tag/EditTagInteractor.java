package use_case.edit_custom_tag;

import entity.CustomTag;

import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.edit_custom_tag.tagReplacement.TagReplacementStrategy;

import java.util.Map;

/**
 * Interactor class implementing the EditTagInputBoundary.
 */
public class EditTagInteractor implements EditTagInputBoundary {

    private final CustomTagDataAccessInterface tagDao;
    private final EditTagOutputBoundary editTagPresenter;
    private final TagReplacementStrategy replacementStrategy;

    public EditTagInteractor(
            CustomTagDataAccessInterface tagDao,
            EditTagOutputBoundary editTagPresenter,
            TagReplacementStrategy replacementStrategy) {

        this.tagDao = tagDao;
        this.editTagPresenter = editTagPresenter;
        this.replacementStrategy = replacementStrategy;
    }

    public void execute(EditTagInputData inputData) {

        final Boolean status = replacementStrategy.replaceTag(inputData, tagDao);
        String statusMsg = replacementStrategy.getStatusMsg();

        // check status
        if (!status) {
            EditTagOutputData failedOutput = new EditTagOutputData(statusMsg);
            editTagPresenter.prepareFailView(failedOutput);

        } else {
            CustomTag newTag = replacementStrategy.getCreatedTag();
            EditTagOutputData successOutput = new EditTagOutputData(newTag);
            editTagPresenter.prepareSuccessView(successOutput);

        }
    }
}
