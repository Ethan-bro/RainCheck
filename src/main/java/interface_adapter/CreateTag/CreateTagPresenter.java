package interface_adapter.CreateTag;

import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.ViewManagerModel;
import use_case.CreateTag.CreateTagOutputBoundary;
import use_case.CreateTag.CreateTagOutputData;

public class CreateTagPresenter implements CreateTagOutputBoundary {

    private ViewManagerModel viewManagerModel;
    private final CreateTagViewModel createCustomTagViewModel;
    private final ManageTagsViewModel manageTagsViewModel;

    public CreateTagPresenter(ViewManagerModel viewManagerModel,
                              CreateTagViewModel createCustomTagViewModel,
                              ManageTagsViewModel manageTagsViewModel) {

        this.viewManagerModel = viewManagerModel;
        this.createCustomTagViewModel = createCustomTagViewModel;
        this.manageTagsViewModel = manageTagsViewModel;
    }


    @Override
    public void prepareFailView(CreateTagOutputData outputData) {
        final CreateTagState customTagState = createCustomTagViewModel.getState();
        customTagState.setErrorMsg(outputData.getErrorMessage());
        customTagState.setCurrTagName(null);    // clear prior tag
        customTagState.setCurrTagEmoji("");
        createCustomTagViewModel.firePropertyChanged("Failed");
    }

    @Override
    public void prepareSuccessView(CreateTagOutputData outputData) {
        final CreateTagState customTagState = createCustomTagViewModel.getState();
        customTagState.setErrorMsg(null);
        customTagState.setCurrTagName(outputData.getCreatedTag().getTagName());
        customTagState.setCurrTagEmoji(outputData.getCreatedTag().getTagIcon());
        createCustomTagViewModel.firePropertyChanged("Success");

        try {
            String username = createCustomTagViewModel.getUsername();
            manageTagsViewModel.setUsername(username); // sets the username and refreshes the tags
            manageTagsViewModel.firePropertyChanged("refreshTagOptions");
        } catch (Exception e) {
            System.err.println("Error while refreshing tag options: " + e.getMessage());
        }
    }
}
