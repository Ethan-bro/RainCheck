package interface_adapter.createTag;

import interface_adapter.ViewManagerModel;
import interface_adapter.manageTags.ManageTagsViewModel;

import use_case.createCustomTag.CreateCustomTagOutputBoundary;
import use_case.createCustomTag.CreateCustomTagOutputData;

public class CreateCustomTagPresenter implements CreateCustomTagOutputBoundary {

    private ViewManagerModel viewManagerModel;
    private final CreateCustomTagViewModel createCustomTagViewModel;
    private final ManageTagsViewModel manageTagsViewModel;

    /**
     * Constructs a CreateCustomTagPresenter.
     *
     * @param viewManagerModel the model managing view state
     * @param createCustomTagViewModel the view model for creating custom tags
     * @param manageTagsViewModel the view model for managing tags
     */
    public CreateCustomTagPresenter(ViewManagerModel viewManagerModel,
                                    CreateCustomTagViewModel createCustomTagViewModel,
                                    ManageTagsViewModel manageTagsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.createCustomTagViewModel = createCustomTagViewModel;
        this.manageTagsViewModel = manageTagsViewModel;
    }

    /**
     * Prepares the view for a failed custom tag creation operation.
     *
     * @param outputData the output data containing error information
     */
    @Override
    public void prepareFailView(CreateCustomTagOutputData outputData) {
        final CreateCustomTagState customTagState = createCustomTagViewModel.getState();
        customTagState.setErrorMsg(outputData.getErrorMessage());
        customTagState.setCurrTagName(null);
        customTagState.setCurrTagEmoji("");
        createCustomTagViewModel.firePropertyChanged("Failed");
    }

    /**
     * Prepares the view for a successful custom tag creation operation.
     *
     * @param outputData the output data for the successful operation
     */
    @Override
    public void prepareSuccessView(CreateCustomTagOutputData outputData) {
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
