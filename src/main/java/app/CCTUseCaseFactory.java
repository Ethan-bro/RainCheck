package app;

import interface_adapter.createTag.CCTController;
import interface_adapter.createTag.CCTPresenter;
import interface_adapter.createTag.CCTViewModel;
import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.ViewManagerModel;

import use_case.createCustomTag.CCTInputBoundary;
import use_case.createCustomTag.CCTInteractor;
import use_case.createCustomTag.CCTOutputBoundary;
import use_case.createCustomTag.CustomTagDataAccessInterface;

import view.CCTView;

public final class CCTUseCaseFactory {

    private CCTUseCaseFactory() {

    }

    public static CCTView create (
            ViewManagerModel viewManagerModel,
            CCTViewModel viewModel,
            ManageTagsViewModel manageTagsViewModel,
            CustomTagDataAccessInterface CTDataAccessInterface) {

        final CCTController controller = createCCTUseCase(viewManagerModel, viewModel, manageTagsViewModel,
                CTDataAccessInterface);

        return new CCTView(viewManagerModel, viewModel, controller);
    }

    public static CCTController createCCTUseCase(ViewManagerModel viewManagerModel,
                                                 CCTViewModel cctViewModel,
                                                 ManageTagsViewModel manageTagsViewModel,
                                                 CustomTagDataAccessInterface customTagDataAccessInterface) {

        final CCTOutputBoundary presenter = new CCTPresenter(viewManagerModel, cctViewModel, manageTagsViewModel);

        final CCTInputBoundary interactor = new CCTInteractor(customTagDataAccessInterface, presenter);

        return new CCTController(interactor);
    }
}
