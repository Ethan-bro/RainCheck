package use_case.deleteCustomTag;

import entity.CustomTag;
import use_case.createCustomTag.CustomTagDataAccessInterface;

/**
 * Interactor for deleting a custom tag.
 */
public final class DeleteCustomTagInteractor implements DeleteCustomTagInputBoundary {

    private final DeleteCustomTagOutputBoundary deleteCtPresenter;
    private final CustomTagDataAccessInterface tagDao;

    /**
     * Constructs a DeleteCustomTagInteractor.
     *
     * @param deleteCtPresenter the output boundary to prepare the response view
     * @param tagDao the data access interface for custom tags
     */
    public DeleteCustomTagInteractor(DeleteCustomTagOutputBoundary deleteCtPresenter,
                                     CustomTagDataAccessInterface tagDao) {
        this.deleteCtPresenter = deleteCtPresenter;
        this.tagDao = tagDao;
    }

    /**
     * Executes the use case for deleting a custom tag.
     *
     * @param inputData the input data containing username and tag to delete
     */
    @Override
    public void execute(DeleteCustomTagInputData inputData) {
        final CustomTag oldTag = inputData.getTag();
        final String username = inputData.getUsername();

        tagDao.deleteCustomTag(username, oldTag);

        final DeleteCustomTagOutputData successOutput = new DeleteCustomTagOutputData("success");
        deleteCtPresenter.prepareSuccessView(successOutput);
    }
}
