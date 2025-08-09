package interface_adapter.DeleteCT;

import interface_adapter.manageTags.ManageTagsViewModel;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import entity.CustomTag;
import use_case.DeleteCustomTag.DeleteCustomTagInputBoundary;
import use_case.DeleteCustomTag.DeleteCustomTagInputData;

public class DeleteTagController {

    private final CustomTagDataAccessInterface tagDao;
    private final ManageTagsViewModel manageTagsVM;
    private final DeleteCustomTagInputBoundary interactor;

    public DeleteTagController(CustomTagDataAccessInterface tagDao,
                               ManageTagsViewModel manageTagsVM,
                               DeleteCustomTagInputBoundary Interactor) {
        this.tagDao = tagDao;
        this.manageTagsVM = manageTagsVM;
        this.interactor = Interactor;
    }

    /**
     * Deletes a custom tag by name.
     *
     * @param tagName name of tag to delete
     */
    public void execute(CustomTag tagToDelete) {
        String username = manageTagsVM.getUsername();

        // Convert user fields into input data and call the interactor
        DeleteCustomTagInputData input = new DeleteCustomTagInputData(username, tagToDelete);
        interactor.execute(input);
    }
}
