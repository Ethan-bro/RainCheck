package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.createTag.cctController;
import interface_adapter.createTag.cctPresenter;
import interface_adapter.createTag.cctViewModel;
import interface_adapter.manageTags.manageTagsViewModel;

import use_case.createCustomTag.cctInputBoundary;
import use_case.createCustomTag.cctInteractor;
import use_case.createCustomTag.cctOutputBoundary;
import use_case.createCustomTag.customTagDataAccessInterface;

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
                                 cctViewModel viewModel,
                                 manageTagsViewModel manageTagsViewModel,
                                 customTagDataAccessInterface customTagDataAccess) {

        final cctController controller = createCctUseCase(viewManagerModel, viewModel,
                manageTagsViewModel, customTagDataAccess);

        return new CCTView(viewManagerModel, viewModel, controller);
    }

    /**
     * Creates the CCT use case controller.
     *
     * @param viewManagerModel the view manager model
     * @param cctViewModel the CCT view model
     * @param manageTagsViewModel the manage tags view model
     * @param customTagDataAccess the custom tag data access interface
     * @return the CCT controller
     */
    public static cctController createCctUseCase(ViewManagerModel viewManagerModel,
                                                 cctViewModel cctViewModel,
                                                 manageTagsViewModel manageTagsViewModel,
                                                 customTagDataAccessInterface customTagDataAccess) {

        final cctOutputBoundary presenter = new cctPresenter(viewManagerModel, cctViewModel, manageTagsViewModel);
        final cctInputBoundary interactor = new cctInteractor(customTagDataAccess, presenter);

        return new cctController(interactor);
    }
}
