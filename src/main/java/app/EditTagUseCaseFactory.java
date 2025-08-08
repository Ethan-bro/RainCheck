package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.editTag.EditTagController;
import interface_adapter.editTag.EditTagPresenter;
import interface_adapter.editTag.EditTagViewModel;
import interface_adapter.manageTags.ManageTagsViewModel;

import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.edit_custom_tag.EditTagInputBoundary;
import use_case.edit_custom_tag.EditTagInteractor;
import use_case.edit_custom_tag.EditTagOutputBoundary;

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
     * @param viewModel the EditTagViewModel instance
     * @param manageTagsViewModel the ManageTagsViewModel instance
     * @param customTagDataAccessInterface the data access interface
     * @return a new EditTagView instance
     */
    public static EditTagView create(
            final ViewManagerModel viewManagerModel,
            final EditTagViewModel viewModel,
            final ManageTagsViewModel manageTagsViewModel,
            final CustomTagDataAccessInterface customTagDataAccessInterface
    ) {
        final EditTagController controller = createEditTagUseCase(viewManagerModel, viewModel,
                manageTagsViewModel, customTagDataAccessInterface);
        return new EditTagView(viewManagerModel, manageTagsViewModel, viewModel, controller);
    }

    /**
     * Creates the EditTagController for the use case.
     *
     * @param viewManagerModel the ViewManagerModel instance
     * @param viewModel the EditTagViewModel instance
     * @param manageTagsViewModel the ManageTagsViewModel instance
     * @param tagDao the data access interface
     * @return a new EditTagController instance
     */
    public static EditTagController createEditTagUseCase(
            final ViewManagerModel viewManagerModel,
            final EditTagViewModel viewModel,
            final ManageTagsViewModel manageTagsViewModel,
            final CustomTagDataAccessInterface tagDao
    ) {
        final EditTagOutputBoundary presenter = new EditTagPresenter(viewManagerModel, viewModel, manageTagsViewModel);
        final EditTagInputBoundary interactor = new EditTagInteractor(tagDao, presenter);
        return new EditTagController(tagDao, manageTagsViewModel, interactor);
    }
}
