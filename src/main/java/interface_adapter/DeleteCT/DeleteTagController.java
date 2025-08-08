package interface_adapter.DeleteCT;

import interface_adapter.ManageTags.ManageTagsViewModel;
import use_case.CreateCT.CustomTagDataAccessInterface;
import entity.CustomTag;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.DeleteCT.DeleteCTInputBoundary;
import use_case.DeleteCT.DeleteCTInputData;
import use_case.EditCT.EditTagInputData;

public class DeleteTagController {

    private final CustomTagDataAccessInterface tagDao;
    private final ManageTagsViewModel manageTagsVM;
    private final DeleteCTInputBoundary Interactor;

    public DeleteTagController(CustomTagDataAccessInterface tagDao,
                               ManageTagsViewModel manageTagsVM,
                               DeleteCTInputBoundary Interactor) {
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
        DeleteCTInputData input = new DeleteCTInputData(username, tagToDelete);
        Interactor.execute(input);
    }
}
