package interface_adapter.editTag;

import entity.CustomTag;

import interface_adapter.manageTags.ManageTagsViewModel;

import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.edit_custom_tag.EditTagInputBoundary;
import use_case.edit_custom_tag.EditTagInputData;

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
     * Executes the edit tag use case.
     *
     * @param oldTag the existing tag to edit
     * @param newTag the updated tag data
     */
    public void execute(CustomTag oldTag, CustomTag newTag) {
        final String username = manageTagsViewModel.getUsername();

        final EditTagInputData input = new EditTagInputData(oldTag, newTag, username);
        interactor.execute(input);
    }
}
