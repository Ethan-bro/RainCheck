package interface_adapter.editTag;

import entity.CustomTag;
import interface_adapter.manageTags.ManageTagsViewModel;
import use_case.edit_custom_tag.EditTagInputBoundary;
import use_case.edit_custom_tag.EditTagInputData;
import use_case.createCustomTag.*;

public class EditTagController {

    private final CustomTagDataAccessInterface tagDao;
    private final ManageTagsViewModel manageTagsVM;
    private final EditTagInputBoundary interactor;

    public EditTagController(CustomTagDataAccessInterface tagDao,
                             ManageTagsViewModel manageTagsVM,
                             EditTagInputBoundary interactor) {
        this.tagDao = tagDao;
        this.manageTagsVM = manageTagsVM;
        this.interactor = interactor;
    }

    /**
     *
     */
    public void execute(CustomTag oldTag, CustomTag newTag) {

        String username = manageTagsVM.getUsername();

        // Convert user fields into input data and call the interactor
        EditTagInputData input = new EditTagInputData(oldTag, newTag, username);
        interactor.execute(input);

    }
}
