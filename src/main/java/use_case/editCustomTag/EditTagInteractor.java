package use_case.editCustomTag;

import entity.CustomTag;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.editCustomTag.TagReplacement.TagReplacementStrategy;

/**
 * Interactor class implementing the EditTagInputBoundary.
 */
public class EditTagInteractor implements EditTagInputBoundary {

    private final CustomTagDataAccessInterface tagDao;
    private final EditTagOutputBoundary editTagPresenter;
    private final TagReplacementStrategy replacementStrategy;

    /**
     * Constructs an EditTagInteractor.
     *
     * @param tagDao the data access interface for custom tags
     * @param editTagPresenter the output boundary to present results
     * @param replacementStrategy the strategy for replacing tags
     */
    public EditTagInteractor(
            CustomTagDataAccessInterface tagDao,
            EditTagOutputBoundary editTagPresenter,
            TagReplacementStrategy replacementStrategy) {
        this.tagDao = tagDao;
        this.editTagPresenter = editTagPresenter;
        this.replacementStrategy = replacementStrategy;
    }

    /**
     * Executes the use case for editing a custom tag.
     *
     * @param inputData the input data containing old and new tags and username
     */
    @Override
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
