package interface_adapter.createTag;

import interface_adapter.manageTags.manageTagsViewModel;
import interface_adapter.ViewManagerModel;
import use_case.createCustomTag.cctOutputBoundary;
import use_case.createCustomTag.cctOutputData;

public class cctPresenter implements cctOutputBoundary {

    private ViewManagerModel viewManagerModel;
    private final cctViewModel createCustomTagViewModel;
    private final manageTagsViewModel manageTagsViewModel;

    public cctPresenter(ViewManagerModel viewManagerModel,
                        cctViewModel createCustomTagViewModel,
                        manageTagsViewModel manageTagsViewModel) {

        this.viewManagerModel = viewManagerModel;
        this.createCustomTagViewModel = createCustomTagViewModel;
        this.manageTagsViewModel = manageTagsViewModel;
    }

    @Override
    public void prepareFailView(cctOutputData outputData) {
        final cctState customTagState = createCustomTagViewModel.getState();
        customTagState.setErrorMsg(outputData.getErrorMessage());
        customTagState.setCurrTagName(null);
        customTagState.setCurrTagEmoji("");
        createCustomTagViewModel.firePropertyChanged("Failed");
    }

    @Override
    public void prepareSuccessView(cctOutputData outputData) {
        final cctState customTagState = createCustomTagViewModel.getState();
        customTagState.setErrorMsg(null);
        customTagState.setCurrTagName(outputData.getCreatedTag().getTagName());
        customTagState.setCurrTagEmoji(outputData.getCreatedTag().getTagIcon());
        createCustomTagViewModel.firePropertyChanged("Success");

        final String username = createCustomTagViewModel.getUsername();
        manageTagsViewModel.setUsername(username);
        manageTagsViewModel.firePropertyChanged("refreshTagOptions");
    }
}
