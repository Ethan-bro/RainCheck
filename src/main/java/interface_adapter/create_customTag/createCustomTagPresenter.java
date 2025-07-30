package interface_adapter.create_customTag;

import use_case.createCustomTag.createCustomTagOutputBoundary;
import use_case.createCustomTag.createCustomTagOutputData;

public class createCustomTagPresenter implements createCustomTagOutputBoundary {

    private final createCustomTagViewModel createCustomTagViewModel;
    // private final AddTaskViewModel addTaskViewModel; -> to be added *later*

    public createCustomTagPresenter(createCustomTagViewModel createCustomTagViewModel) {
        this.createCustomTagViewModel = createCustomTagViewModel;
    }


    @Override
    public void prepareFailView(createCustomTagOutputData outputData) {
        final createCustomTagState customTagState = createCustomTagViewModel.getState();
        customTagState.setErrorMsg(outputData.getErrorMessage());
        customTagState.setCurrTagName(null);    // clear prior tag
        customTagState.setCurrTagEmoji("");
        createCustomTagViewModel.firePropertyChanged("Failed");
    }

    @Override
    public void prepareSuccessView(createCustomTagOutputData outputData) {
        final createCustomTagState customTagState = createCustomTagViewModel.getState();
        customTagState.setErrorMsg(null);
        customTagState.setCurrTagName(outputData.getCreatedTag().getTagName());
        customTagState.setCurrTagEmoji(outputData.getCreatedTag().getTagEmoji());
        createCustomTagViewModel.firePropertyChanged("Success");
    }
}
