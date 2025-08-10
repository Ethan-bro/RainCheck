package interface_adapter.EditTag;

import entity.CustomTag;
import interface_adapter.ManageTags.ManageTagsViewModel;
import use_case.EditTag.EditTagInputBoundary;
import use_case.EditTag.EditTagInputData;
import use_case.CreateTag.*;

public class EditTagController {

    private final TagDataAccessInterface tagDao;
    private final ManageTagsViewModel manageTagsVM;
    private final EditTagInputBoundary Interactor;

    public EditTagController(TagDataAccessInterface tagDao,
                             ManageTagsViewModel manageTagsVM,
                             EditTagInputBoundary Interactor) {

        this.tagDao = tagDao;
        this.manageTagsVM = manageTagsVM;
        this.Interactor = Interactor;
    }

    /**
     *
     */
    public void execute(CustomTag oldTag, CustomTag newTag) {

        String username = manageTagsVM.getUsername();

        // Convert user fields into input data and call the Interactor
        EditTagInputData input = new EditTagInputData(oldTag, newTag, username);
        Interactor.execute(input);

    }
}
