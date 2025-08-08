package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.createTag.CreateCustomTagController;
import interface_adapter.createTag.CreateCustomTagPresenter;
import interface_adapter.createTag.CreateCustomTagViewModel;
import interface_adapter.manageTags.ManageTagsViewModel;

import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.createCustomTag.cctInputBoundary;
import use_case.createCustomTag.cctInteractor;
import use_case.createCustomTag.cctOutputBoundary;

import view.CCTView;

/**
 * Factory class to create CCT use case components.
 */
public final class CctUseCaseFactory {

    private CctUseCaseFactory() {
        // Prevent instantiation
    }

    /**
     * Creates the CCT view with its dependencies.
     *
     * @param viewManagerModel the view manager model
     * @param viewModel the CCT view model
     * @param manageTagsViewModel the manage tags view model
     * @param customTagDataAccess the custom tag data access interface
     * @return the created CCT view
     */
    public static CCTView create(ViewManagerModel viewManagerModel,
                                 CreateCustomTagViewModel viewModel,
                                 ManageTagsViewModel manageTagsViewModel,
                                 CustomTagDataAccessInterface customTagDataAccess) {

        final CreateCustomTagController controller = createCctUseCase(viewManagerModel, viewModel,
                manageTagsViewModel, customTagDataAccess);

        return new CCTView(viewManagerModel, viewModel, controller);
    }

    /**
     * Creates the CCT use case controller.
     *
     * @param viewManagerModel the view manager model
     * @param CreateCustomTagViewModel the CCT view model
     * @param manageTagsViewModel the manage tags view model
     * @param customTagDataAccess the custom tag data access interface
     * @return the CCT controller
     */
    public static CreateCustomTagController createCctUseCase(ViewManagerModel viewManagerModel,
                                                             CreateCustomTagViewModel CreateCustomTagViewModel,
                                                             ManageTagsViewModel manageTagsViewModel,
                                                             CustomTagDataAccessInterface customTagDataAccess) {

        final cctOutputBoundary presenter = new CreateCustomTagPresenter(
                viewManagerModel, CreateCustomTagViewModel, manageTagsViewModel
        );
        final cctInputBoundary interactor = new cctInteractor(customTagDataAccess, presenter);

        return new CreateCustomTagController(interactor);
    }
}
