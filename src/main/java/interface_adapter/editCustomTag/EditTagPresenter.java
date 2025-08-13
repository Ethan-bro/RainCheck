package interface_adapter.editCustomTag;

import interface_adapter.ViewManagerModel;
import interface_adapter.events.TagChangeEventNotifier;
import interface_adapter.manageTags.ManageTagsViewModel;
import use_case.editCustomTag.EditTagOutputBoundary;
import use_case.editCustomTag.EditTagOutputData;

/**
 * Presenter for Edit Tag feature that updates ViewModels and handles success/failure views.
 */
public class EditTagPresenter implements EditTagOutputBoundary {

    private ViewManagerModel viewManagerModel;
    private EditTagViewModel editTagViewModel;
    private ManageTagsViewModel manageTagsViewModel;


    /**
     * Constructs an EditTagPresenter with the given view models.
     *
     * @param viewManagerModel the view manager model
     * @param editTagViewModel the edit tag view model
     * @param manageTagsViewModel the manage tags view model
     */
    public EditTagPresenter(ViewManagerModel viewManagerModel,
                            EditTagViewModel editTagViewModel,
                            ManageTagsViewModel manageTagsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.manageTagsViewModel = manageTagsViewModel;
        this.editTagViewModel = editTagViewModel;
    }


    /**
     * Returns the view manager model.
     *
     * @return the view manager model
     */
    public ViewManagerModel getViewManagerModel() {
        return viewManagerModel;
    }


    /**
     * Sets the view manager model.
     *
     * @param viewManagerModel the view manager model to set
     */
    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }


    /**
     * Returns the edit tag view model.
     *
     * @return the edit tag view model
     */
    public EditTagViewModel getEditTagViewModel() {
        return editTagViewModel;
    }


    /**
     * Sets the edit tag view model.
     *
     * @param editTagViewModel the edit tag view model to set
     */
    public void setEditTagViewModel(EditTagViewModel editTagViewModel) {
        this.editTagViewModel = editTagViewModel;
    }


    /**
     * Returns the manage tags view model.
     *
     * @return the manage tags view model
     */
    public ManageTagsViewModel getManageTagsViewModel() {
        return manageTagsViewModel;
    }


    /**
     * Sets the manage tags view model.
     *
     * @param manageTagsViewModel the manage tags view model to set
     */
    public void setManageTagsViewModel(ManageTagsViewModel manageTagsViewModel) {
        this.manageTagsViewModel = manageTagsViewModel;
    }


    /**
     * Prepares the view for a successful tag edit.
     * Updates the state and notifies the relevant view models.
     *
     * @param successOutput the output data from the edit tag use case
     */
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


    /**
     * Prepares the view for a failed tag edit.
     * Updates the state and notifies the relevant view models.
     *
     * @param failedOutput the output data containing the error message
     */
    @Override
    public void prepareFailView(EditTagOutputData failedOutput) {
        final EditTagState editTagState = editTagViewModel.getState();
        editTagState.setErrorMsg(failedOutput.getErrorMessage());
        editTagState.setCurrTagName(null);
        editTagState.setCurrTagEmoji("");
        editTagViewModel.firePropertyChanged("Failed");
    }
}
