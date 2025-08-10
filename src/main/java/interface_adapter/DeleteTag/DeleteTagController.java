package interface_adapter.DeleteTag;

import interface_adapter.ManageTags.ManageTagsViewModel;
import use_case.CreateTag.TagDataAccessInterface;
import entity.CustomTag;
import use_case.DeleteTag.DeleteTagInputBoundary;
import use_case.DeleteTag.DeleteTagInputData;

public class DeleteTagController {

    private final TagDataAccessInterface tagDao;
    private final ManageTagsViewModel manageTagsVM;
    private final DeleteTagInputBoundary Interactor;

    public DeleteTagController(TagDataAccessInterface tagDao,
                               ManageTagsViewModel manageTagsVM,
                               DeleteTagInputBoundary Interactor) {
        this.tagDao = tagDao;
        this.manageTagsVM = manageTagsVM;
        this.Interactor = Interactor;
    }

    /**
     * Deletes a custom tag by name.
     *
     * @param tagName name of tag to delete
     */
    public void execute(CustomTag tagToDelete) {
        String username = manageTagsVM.getUsername();

        // Convert user fields into input data and call the Interactor
        DeleteTagInputData input = new DeleteTagInputData(username, tagToDelete);
        Interactor.execute(input);
    }
}
