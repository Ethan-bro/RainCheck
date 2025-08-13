package interface_adapter.editCustomTag;

import entity.CustomTag;

import interface_adapter.manageTags.ManageTagsViewModel;

import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.editCustomTag.EditTagInputBoundary;
import use_case.editCustomTag.EditTagInputData;

public class EditTagController {

    private final CustomTagDataAccessInterface tagDao;
    private final ManageTagsViewModel manageTagsViewModel;
    private final EditTagInputBoundary interactor;

    /**
     * Constructs an EditTagController with the required dependencies.
     *
     * @param tagDao the data access object for custom tags
     * @param manageTagsViewModel the view model managing tag-related UI state
     * @param interactor the input boundary for the edit tag use case
     */
    public EditTagController(CustomTagDataAccessInterface tagDao,
                             ManageTagsViewModel manageTagsViewModel,
                             EditTagInputBoundary interactor) {
        this.tagDao = tagDao;
        this.manageTagsViewModel = manageTagsViewModel;
        this.interactor = interactor;
    }

    /**
     * Executes the edit tag operation.
     *
     * @param oldTag existing tag to be edited
     * @param newName the modified tag name
     * @param newIcon the modified tag icon
     */
    public void execute(CustomTag oldTag,
                        String newName,
                        String newIcon) {
        final String username = manageTagsViewModel.getUsername();

        final EditTagInputData input = new EditTagInputData(oldTag,
                newName,
                newIcon,
                username);

        interactor.execute(input);
    }
}
