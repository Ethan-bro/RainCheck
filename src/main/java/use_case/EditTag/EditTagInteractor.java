package use_case.EditTag;

import entity.CustomTag;
import use_case.CreateTag.TagDataAccessInterface;
import use_case.EditTag.TagReplacement.TagReplacementStrategy;

import java.util.Map;

import static use_case.EditTag.TagErrorConstants.ICON_TAKEN_ERROR;
import static use_case.EditTag.TagErrorConstants.NAME_TAKEN_ERROR;

public class EditTagInteractor implements EditTagInputBoundary {

    private final TagDataAccessInterface tagDao;
    private final EditTagOutputBoundary editTagPresenter;
    private final TagReplacementStrategy replacementStrategy;

    public EditTagInteractor(
            TagDataAccessInterface tagDao,
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
            return;
        } else {
            CustomTag newTag = replacementStrategy.getCreatedTag();
            EditTagOutputData successOutput = new EditTagOutputData(newTag);
            editTagPresenter.prepareSuccessView(successOutput);

        }
    }
}
