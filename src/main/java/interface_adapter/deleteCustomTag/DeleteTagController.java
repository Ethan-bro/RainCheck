package interface_adapter.deleteCustomTag;

import entity.CustomTag;

import interface_adapter.manageTags.ManageTagsViewModel;

import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.deleteCustomTag.DeleteCustomTagInputBoundary;
import use_case.deleteCustomTag.DeleteCustomTagInputData;

public class DeleteTagController {

    private final CustomTagDataAccessInterface tagDao;
    private final ManageTagsViewModel manageTagsViewModel;
    private final DeleteCustomTagInputBoundary interactor;

    public DeleteTagController(CustomTagDataAccessInterface tagDao,
                               ManageTagsViewModel manageTagsViewModel,
                               DeleteCustomTagInputBoundary interactor) {
        this.tagDao = tagDao;
        this.manageTagsViewModel = manageTagsViewModel;
        this.interactor = interactor;
    }

    /**
     * Deletes a custom tag.
     *
     * @param tagToDelete the tag to delete
     */
    public void execute(CustomTag tagToDelete) {
        final String username = manageTagsViewModel.getUsername();

        final DeleteCustomTagInputData input = new DeleteCustomTagInputData(username, tagToDelete);
        interactor.execute(input);
    }
}
