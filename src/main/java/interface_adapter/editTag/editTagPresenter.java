package interface_adapter.editTag;

import interface_adapter.manageTags.manageTagsViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.EditCT.EditTagOutputBoundary;
import use_case.EditCT.EditTagOutputData;

public class editTagPresenter implements EditTagOutputBoundary {

    ViewManagerModel viewManagerModel;
    editTagViewModel editTagViewModel;
    manageTagsViewModel manageTagsViewModel;


    public editTagPresenter(ViewManagerModel viewManagerModel,
                            editTagViewModel editTagViewModel,
                            manageTagsViewModel manageTagsViewModel) {

        this.viewManagerModel = viewManagerModel;
        this.manageTagsViewModel = manageTagsViewModel;
        this.editTagViewModel = editTagViewModel;
    }


    @Override
    public void prepareSuccessView(EditTagOutputData successOutput) {
        final editTagState editTagState = editTagViewModel.getState();
        editTagState.setErrorMsg(null);
        editTagState.setCurrTagName(successOutput.getCreatedTag().getTagName());
        editTagState.setCurrTagEmoji(successOutput.getCreatedTag().getTagIcon());
        editTagViewModel.firePropertyChanged("Success");

        try {
            String username = editTagViewModel.getUsername();
            manageTagsViewModel.setUsername(username); // sets the username and refreshes the tags
            manageTagsViewModel.firePropertyChanged("refreshTagOptions");
        } catch (Exception e) {
            System.err.println("Error while refreshing tag options: " + e.getMessage());
        }

        // notify all subscribed views/viewModels of tag changes
        TagChangeEventNotifier.fire();
    }

    @Override
    public void prepareFailView(EditTagOutputData failedOutput) {
        final editTagState editTagState = editTagViewModel.getState();
        editTagState.setErrorMsg(failedOutput.getErrorMessage());
        editTagState.setCurrTagName(null);    // clear prior tag
        editTagState.setCurrTagEmoji("");
        editTagViewModel.firePropertyChanged("Failed");
    }
}
