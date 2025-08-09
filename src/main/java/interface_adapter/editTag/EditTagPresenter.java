package interface_adapter.editTag;

import interface_adapter.ViewManagerModel;
import interface_adapter.events.TagChangeEventNotifier;
import interface_adapter.manageTags.ManageTagsViewModel;

import use_case.edit_custom_tag.EditTagOutputBoundary;
import use_case.edit_custom_tag.EditTagOutputData;

/**
 * Presenter for Edit Tag feature that updates ViewModels and handles success/failure views.
 */
public class EditTagPresenter implements EditTagOutputBoundary {

    private ViewManagerModel viewManagerModel;
    private EditTagViewModel editTagViewModel;
    private ManageTagsViewModel manageTagsViewModel;

    public EditTagPresenter(ViewManagerModel viewManagerModel,
                            EditTagViewModel editTagViewModel,
                            ManageTagsViewModel manageTagsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.manageTagsViewModel = manageTagsViewModel;
        this.editTagViewModel = editTagViewModel;
    }

    public ViewManagerModel getViewManagerModel() {
        return viewManagerModel;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public EditTagViewModel getEditTagViewModel() {
        return editTagViewModel;
    }

    public void setEditTagViewModel(EditTagViewModel editTagViewModel) {
        this.editTagViewModel = editTagViewModel;
    }

    public ManageTagsViewModel getManageTagsViewModel() {
        return manageTagsViewModel;
    }

    public void setManageTagsViewModel(ManageTagsViewModel manageTagsViewModel) {
        this.manageTagsViewModel = manageTagsViewModel;
    }

    @Override
    public void prepareSuccessView(EditTagOutputData successOutput) {
        final EditTagState editTagState = editTagViewModel.getState();
        editTagState.setErrorMsg(null);
        editTagState.setCurrTagName(successOutput.getCreatedTag().getTagName());
        editTagState.setCurrTagEmoji(successOutput.getCreatedTag().getTagIcon());
        editTagViewModel.firePropertyChanged("Success");

        final String username = editTagViewModel.getUsername();

        manageTagsViewModel.setUsername(username);
        manageTagsViewModel.firePropertyChanged("refreshTagOptions");

        TagChangeEventNotifier.fire();
    }

    @Override
    public void prepareFailView(EditTagOutputData failedOutput) {
        final EditTagState editTagState = editTagViewModel.getState();
        editTagState.setErrorMsg(failedOutput.getErrorMessage());
        editTagState.setCurrTagName(null);
        editTagState.setCurrTagEmoji("");
        editTagViewModel.firePropertyChanged("Failed");
    }
}
