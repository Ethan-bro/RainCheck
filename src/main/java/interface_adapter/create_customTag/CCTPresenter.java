package interface_adapter.create_customTag;

import entity.CustomTag;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskViewModel;
import use_case.createCustomTag.CCTOutputBoundary;
import use_case.createCustomTag.CCTOutputData;
import use_case.createCustomTag.CustomTagDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CCTPresenter implements CCTOutputBoundary {

    private ViewManagerModel viewManagerModel;
    private final CCTViewModel createCustomTagViewModel;
    private final AddTaskViewModel addTaskViewModel;

    public CCTPresenter(
            ViewManagerModel viewManagerModel,
            CCTViewModel createCustomTagViewModel,
            AddTaskViewModel addTaskViewModel) {

        this.viewManagerModel = viewManagerModel;
        this.createCustomTagViewModel = createCustomTagViewModel;
        this.addTaskViewModel = addTaskViewModel;

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

        addTaskViewModel.refreshTags();

        createCustomTagViewModel.firePropertyChanged("Success");
    }
}
