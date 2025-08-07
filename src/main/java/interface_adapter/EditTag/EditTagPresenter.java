package interface_adapter.EditTag;

import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.EditCT.EditTagOutputBoundary;
import use_case.EditCT.EditTagOutputData;

public class EditTagPresenter implements EditTagOutputBoundary {

    EditTagOutputData editTagOutputData;
    EditTagViewModel editTagViewModel;
    ManageTagsViewModel manageTagsViewModel;


    public EditTagPresenter(EditTagOutputData editTagOutputData,
                            EditTagViewModel editTagViewModel,
                            ManageTagsViewModel manageTagsViewModel) {

        this.editTagOutputData = editTagOutputData;
        this.editTagViewModel = editTagViewModel;
    }


    @Override
    public void prepareSuccessView(EditTagOutputData successOutput) {
        final EditTagState editTagState = editTagViewModel.getState();
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
        final EditTagState editTagState = editTagViewModel.getState();
        editTagState.setErrorMsg(failedOutput.getErrorMessage());
        editTagState.setCurrTagName(null);    // clear prior tag
        editTagState.setCurrTagEmoji("");
        editTagViewModel.firePropertyChanged("Failed");
    }
}
