package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_customTag.CCTController;
import interface_adapter.create_customTag.CCTPresenter;
import interface_adapter.create_customTag.CCTViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
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
            CustomTagDataAccessInterface CTDataAccessInterface,
            LoggedInViewModel loggedInViewModel) {

        final CCTController Controller = createCCTUseCase(viewManagerModel, viewModel,
                CTDataAccessInterface);

        return new CCTView(viewModel, Controller, loggedInViewModel);
    }

    public static CCTController createCCTUseCase(ViewManagerModel viewManagerModel,
                                                 CCTViewModel viewModel,
                                                 CustomTagDataAccessInterface customTagDataAccessInterface) {

        final CCTOutputBoundary Presenter = new CCTPresenter(viewManagerModel, viewModel);

        final CCTInputBoundary Interactor = new CCTInteractor(customTagDataAccessInterface, Presenter);

        return new CCTController(Interactor);
    }

}