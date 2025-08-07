package interface_adapter.EditTag;

import entity.CustomTag;
import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.CreateTag.CCTController;
import interface_adapter.CreateTag.CCTPresenter;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.EditCT.EditTagInputBoundary;
import use_case.EditCT.EditTagInputData;
import use_case.createCustomTag.*;

public class EditTagController {

    private final CustomTagDataAccessInterface tagDao;
    private final ManageTagsViewModel manageTagsVM;
    private final EditTagInputBoundary Interactor;

    public EditTagController(CustomTagDataAccessInterface tagDao,
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
