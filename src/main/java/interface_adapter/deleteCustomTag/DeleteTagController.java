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

    /**
     * Constructs a DeleteTagController with the required dependencies for tag deletion.
     * This controller coordinates the interaction between the view model, data access, and use case interactor,
     * adhering to Clean Architecture by separating concerns and promoting dependency inversion.
     *
     * @param tagDao the data access object for custom tags
     * @param manageTagsViewModel the view model managing tag-related UI state
     * @param interactor the input boundary for the delete custom tag use case
     */
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
