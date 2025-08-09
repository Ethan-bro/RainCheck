package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.createTag.CreateCustomTagController;
import interface_adapter.createTag.CreateCustomTagPresenter;
import interface_adapter.createTag.CreateCustomTagViewModel;
import interface_adapter.manageTags.ManageTagsViewModel;

import use_case.createCustomTag.CreateCustomTagInputBoundary;
import use_case.createCustomTag.CreateCustomTagInteractor;
import use_case.createCustomTag.CreateCustomTagOutputBoundary;
import use_case.createCustomTag.CustomTagDataAccessInterface;

import view.CreateCustomTagView;

/**
 * Factory class to create the Create Custom Tag use case components.
 */
public final class CctUseCaseFactory {

    private CctUseCaseFactory() {

    }

    /**
     * Creates the CreateCustomTagView with all its dependencies.
     *
     * @param viewManagerModel       the view manager model
     * @param viewModel              the create custom tag view model
     * @param manageTagsViewModel    the manage tags view model
     * @param customTagDataAccess    the custom tag data access interface
     * @return the CreateCustomTagView instance
     */
    public static CreateCustomTagView create(
            ViewManagerModel viewManagerModel,
            CreateCustomTagViewModel viewModel,
            ManageTagsViewModel manageTagsViewModel,
            CustomTagDataAccessInterface customTagDataAccess) {

        final CreateCustomTagController controller = createCctUseCase(viewManagerModel, viewModel, manageTagsViewModel,
                customTagDataAccess);

        return new CreateCustomTagView(viewManagerModel, viewModel, controller);
    }

    /**
     * Creates the CreateCustomTagController with its interactor and presenter.
     *
     * @param viewManagerModel       the view manager model
     * @param cctViewModel           the create custom tag view model
     * @param manageTagsViewModel    the manage tags view model
     * @param customTagDataAccess    the custom tag data access interface
     * @return the CreateCustomTagController instance
     */
    public static CreateCustomTagController createCctUseCase(
            ViewManagerModel viewManagerModel,
            CreateCustomTagViewModel cctViewModel,
            ManageTagsViewModel manageTagsViewModel,
            CustomTagDataAccessInterface customTagDataAccess) {

        final CreateCustomTagOutputBoundary presenter = new CreateCustomTagPresenter(viewManagerModel, cctViewModel,
                manageTagsViewModel);

        final CreateCustomTagInputBoundary interactor = new CreateCustomTagInteractor(customTagDataAccess, presenter);

        return new CreateCustomTagController(interactor);
    }
}
