package use_case.DeleteTag;

import entity.CustomTag;
import use_case.CreateTag.TagDataAccessInterface;

public class DeleteTagInteractor implements DeleteTagInputBoundary {

    private final DeleteTagOutputBoundary deleteCTPresenter;
    private final TagDataAccessInterface tagDao;

    public DeleteTagInteractor(DeleteTagOutputBoundary deleteCTPresenter,
                               TagDataAccessInterface tagDao) {

        this.deleteCTPresenter = deleteCTPresenter;
        this.tagDao = tagDao;
    }

    public void execute(DeleteTagInputData inputData) {

        CustomTag oldTag = inputData.getTag();
        String username = inputData.getUsername();

        tagDao.deleteCustomTag(username, oldTag);

        DeleteTagOutputData successOutput = new DeleteTagOutputData("success");
        deleteCTPresenter.prepareSuccessView(successOutput);
    }
}
