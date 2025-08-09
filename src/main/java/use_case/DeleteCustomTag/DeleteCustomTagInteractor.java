package use_case.DeleteCustomTag;

import entity.CustomTag;

import use_case.createCustomTag.CustomTagDataAccessInterface;

public class DeleteCustomTagInteractor implements DeleteCustomTagInputBoundary {

    private final DeleteCustomTagOutputBoundary deleteCTPresenter;
    private final CustomTagDataAccessInterface tagDao;

    public DeleteCustomTagInteractor(DeleteCustomTagOutputBoundary deleteCTPresenter,
                                     CustomTagDataAccessInterface tagDao) {

        this.deleteCTPresenter = deleteCTPresenter;
        this.tagDao = tagDao;
    }

    public void execute(DeleteCustomTagInputData inputData) {

        CustomTag oldTag = inputData.getTag();
        String username = inputData.getUsername();

        tagDao.deleteCustomTag(username, oldTag);

        DeleteCustomTagOutputData successOutput = new DeleteCustomTagOutputData("success");
        deleteCTPresenter.prepareSuccessView(successOutput);
    }
}
