package interface_adapter.create_customTag;

import interface_adapter.ViewManagerModel;
import use_case.createCustomTag.CCTOutputBoundary;
import use_case.createCustomTag.CCTOutputData;

public class CCTPresenter implements CCTOutputBoundary {

    private ViewManagerModel viewManagerModel;
    private final CCTViewModel createCustomTagViewModel;

    public CCTPresenter(ViewManagerModel viewManagerModel,
                        CCTViewModel createCustomTagViewModel) {

        this.viewManagerModel = viewManagerModel;
        this.createCustomTagViewModel = createCustomTagViewModel;
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
        customTagState.setCurrTagEmoji(outputData.getCreatedTag().getTagEmoji());
        createCustomTagViewModel.firePropertyChanged("Success");
    }
}
