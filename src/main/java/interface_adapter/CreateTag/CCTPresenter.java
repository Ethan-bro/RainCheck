package interface_adapter.CreateTag;

import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.ViewManagerModel;
import use_case.createCustomTag.CCTOutputBoundary;
import use_case.createCustomTag.CCTOutputData;

public class CCTPresenter implements CCTOutputBoundary {

    private ViewManagerModel viewManagerModel;
    private final CCTViewModel createCustomTagViewModel;
    private final ManageTagsViewModel manageTagsViewModel;

    public CCTPresenter(ViewManagerModel viewManagerModel,
                        CCTViewModel createCustomTagViewModel,
                        ManageTagsViewModel manageTagsViewModel) {

        this.viewManagerModel = viewManagerModel;
        this.createCustomTagViewModel = createCustomTagViewModel;
        this.manageTagsViewModel = manageTagsViewModel;
    }


    @Override
    public void prepareFailView(CCTOutputData outputData) {
        final CCTState customTagState = createCustomTagViewModel.getState();
        customTagState.setErrorMsg(outputData.getErrorMessage());
        customTagState.setCurrTagName(null);    // clear prior tag
        customTagState.setCurrTagEmoji("");
        createCustomTagViewModel.firePropertyChanged("Failed");
    }

    @Override
    public void prepareSuccessView(CCTOutputData outputData) {
        final CCTState customTagState = createCustomTagViewModel.getState();
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
