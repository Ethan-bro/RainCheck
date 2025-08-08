package interface_adapter.createTag;

import interface_adapter.ViewManagerModel;
import interface_adapter.manageTags.ManageTagsViewModel;

import use_case.createCustomTag.cctOutputBoundary;
import use_case.createCustomTag.cctOutputData;

public class CreateCustomTagPresenter implements cctOutputBoundary {

    private ViewManagerModel viewManagerModel;
    private final CreateCustomTagViewModel createCustomTagViewModel;
    private final ManageTagsViewModel manageTagsViewModel;

    public CreateCustomTagPresenter(ViewManagerModel viewManagerModel,
                                    CreateCustomTagViewModel createCustomTagViewModel,
                                    ManageTagsViewModel manageTagsViewModel) {

        this.viewManagerModel = viewManagerModel;
        this.createCustomTagViewModel = createCustomTagViewModel;
        this.manageTagsViewModel = manageTagsViewModel;
    }

    @Override
    public void prepareFailView(cctOutputData outputData) {
        final CreateCustomTagState customTagState = createCustomTagViewModel.getState();
        customTagState.setErrorMsg(outputData.getErrorMessage());
        customTagState.setCurrTagName(null);
        customTagState.setCurrTagEmoji("");
        createCustomTagViewModel.firePropertyChanged("Failed");
    }

    @Override
    public void prepareSuccessView(cctOutputData outputData) {
        final CreateCustomTagState customTagState = createCustomTagViewModel.getState();
        customTagState.setErrorMsg(null);
        customTagState.setCurrTagName(outputData.getCreatedTag().getTagName());
        customTagState.setCurrTagEmoji(outputData.getCreatedTag().getTagIcon());
        createCustomTagViewModel.firePropertyChanged("Success");

        final String username = createCustomTagViewModel.getUsername();
        manageTagsViewModel.setUsername(username);
        manageTagsViewModel.firePropertyChanged("refreshTagOptions");
    }
}
