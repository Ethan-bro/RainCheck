package use_case.DeleteCT;

import entity.CustomTag;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.EditCT.EditTagOutputData;

public class DeleteCTInteractor implements DeleteCTInputBoundary {

    private final DeleteCTOutputBoundary deleteCTPresenter;
    private final CustomTagDataAccessInterface tagDao;

    public DeleteCTInteractor(DeleteCTOutputBoundary deleteCTPresenter,
                              CustomTagDataAccessInterface tagDao) {

        this.deleteCTPresenter = deleteCTPresenter;
        this.tagDao = tagDao;
    }

    public void execute(DeleteCTInputData inputData) {

        CustomTag oldTag = inputData.getTag();
        String username = inputData.getUsername();

        tagDao.deleteCustomTag(username, oldTag);

        DeleteCTOutputData successOutput = new DeleteCTOutputData("success");
        deleteCTPresenter.prepareSuccessView(successOutput);
    }
}
