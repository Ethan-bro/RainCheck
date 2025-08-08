package interface_adapter.editTag;

import entity.CustomTag;
import interface_adapter.manageTags.manageTagsViewModel;
import use_case.EditCT.EditTagInputBoundary;
import use_case.EditCT.EditTagInputData;
import use_case.createCustomTag.*;

public class editTagController {

    private final customTagDataAccessInterface tagDao;
    private final manageTagsViewModel manageTagsVM;
    private final EditTagInputBoundary Interactor;

    public editTagController(customTagDataAccessInterface tagDao,
                             manageTagsViewModel manageTagsVM,
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
