package interface_adapter.editTag;

import entity.CustomTag;

import interface_adapter.manageTags.ManageTagsViewModel;

import use_case.EditCT.EditTagInputBoundary;
import use_case.EditCT.EditTagInputData;
import use_case.createCustomTag.CustomTagDataAccessInterface;

/**
 * Controller for editing a custom tag.
 */
public class EditTagController {

    private final CustomTagDataAccessInterface tagDao;
    private final ManageTagsViewModel manageTagsViewModel;
    private final EditTagInputBoundary interactor;

    public EditTagController(CustomTagDataAccessInterface tagDao,
                             ManageTagsViewModel manageTagsViewModel,
                             EditTagInputBoundary interactor) {
        this.tagDao = tagDao;
        this.manageTagsViewModel = manageTagsViewModel;
        this.interactor = interactor;
    }

    /**
     * Executes the tag editing process by passing old and new tag information to the interactor.
     *
     * @param oldTag the original tag before editing
     * @param newTag the updated tag after editing
     */
    public void execute(CustomTag oldTag, CustomTag newTag) {
        final String username = manageTagsViewModel.getUsername();

        final EditTagInputData input = new EditTagInputData(oldTag, newTag, username);
        interactor.execute(input);
    }
}
