package app;

import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.CreateTag.CCTController;
import interface_adapter.CreateTag.CCTPresenter;
import interface_adapter.CreateTag.CCTViewModel;
import use_case.createCustomTag.CCTInputBoundary;
import use_case.createCustomTag.CCTInteractor;
import use_case.createCustomTag.CCTOutputBoundary;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import view.CCTView;

public class CCTUseCaseFactory {

    private CCTUseCaseFactory() {}

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

        final CCTOutputBoundary Presenter = new CCTPresenter(viewManagerModel, cctViewModel, manageTagsViewModel);

        final CCTInputBoundary Interactor = new CCTInteractor(customTagDataAccessInterface, Presenter);

        return new CCTController(Interactor);
    }

}