package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.editTag.editTagController;
import interface_adapter.editTag.editTagPresenter;
import interface_adapter.editTag.editTagViewModel;
import interface_adapter.manageTags.manageTagsViewModel;

import use_case.EditCT.EditTagInputBoundary;
import use_case.EditCT.EditTagInteractor;
import use_case.EditCT.EditTagOutputBoundary;
import use_case.createCustomTag.customTagDataAccessInterface;

import view.EditTagView;

/**
 * Factory class for creating Edit Tag use case components.
 */
public final class EditTagUseCaseFactory {

    private EditTagUseCaseFactory() {
        // Prevent instantiation
    }

    /**
     * Creates the EditTagView along with its controller and view models.
     *
     * @param viewManagerModel the ViewManagerModel instance
     * @param viewModel the editTagViewModel instance
     * @param manageTagsViewModel the manageTagsViewModel instance
     * @param customTagDataAccessInterface the data access interface
     * @return a new EditTagView instance
     */
    public static EditTagView create(
            final ViewManagerModel viewManagerModel,
            final editTagViewModel viewModel,
            final manageTagsViewModel manageTagsViewModel,
            final customTagDataAccessInterface customTagDataAccessInterface
    ) {
        final editTagController controller = createEditTagUseCase(viewManagerModel, viewModel,
                manageTagsViewModel, customTagDataAccessInterface);
        return new EditTagView(viewManagerModel, manageTagsViewModel, viewModel, controller);
    }

    /**
     * Creates the editTagController for the use case.
     *
     * @param viewManagerModel the ViewManagerModel instance
     * @param viewModel the editTagViewModel instance
     * @param manageTagsViewModel the manageTagsViewModel instance
     * @param tagDao the data access interface
     * @return a new editTagController instance
     */
    public static editTagController createEditTagUseCase(
            final ViewManagerModel viewManagerModel,
            final editTagViewModel viewModel,
            final manageTagsViewModel manageTagsViewModel,
            final customTagDataAccessInterface tagDao
    ) {
        final EditTagOutputBoundary presenter = new editTagPresenter(viewManagerModel, viewModel, manageTagsViewModel);
        final EditTagInputBoundary interactor = new EditTagInteractor(tagDao, presenter);
        return new editTagController(tagDao, manageTagsViewModel, interactor);
    }
}
