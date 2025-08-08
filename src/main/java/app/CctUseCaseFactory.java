package app;

import interface_adapter.createTag.cctController;
import interface_adapter.createTag.cctPresenter;
import interface_adapter.createTag.cctViewModel;
import interface_adapter.manageTags.manageTagsViewModel;
import interface_adapter.ViewManagerModel;

import use_case.createCustomTag.cctInputBoundary;
import use_case.createCustomTag.cctInteractor;
import use_case.createCustomTag.cctOutputBoundary;
import use_case.createCustomTag.customTagDataAccessInterface;

import view.CCTView;

public final class CCTUseCaseFactory {

    private CCTUseCaseFactory() {

    }

    public static CCTView create (
            ViewManagerModel viewManagerModel,
            cctViewModel viewModel,
            manageTagsViewModel manageTagsViewModel,
            customTagDataAccessInterface CTDataAccessInterface) {

        final cctController controller = createCCTUseCase(viewManagerModel, viewModel, manageTagsViewModel,
                CTDataAccessInterface);

        return new CCTView(viewManagerModel, viewModel, controller);
    }

    public static cctController createCCTUseCase(ViewManagerModel viewManagerModel,
                                                 cctViewModel cctViewModel,
                                                 manageTagsViewModel manageTagsViewModel,
                                                 customTagDataAccessInterface customTagDataAccessInterface) {

        final cctOutputBoundary presenter = new cctPresenter(viewManagerModel, cctViewModel, manageTagsViewModel);

        final cctInputBoundary interactor = new cctInteractor(customTagDataAccessInterface, presenter);

        return new cctController(interactor);
    }
}
