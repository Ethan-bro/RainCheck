package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutUserDataAccessInterface;

public class LogoutUseCaseFactory {

    private LogoutUseCaseFactory() {}

    public static LogoutController create(ViewManagerModel viewManagerModel,
                                          LoggedInViewModel loggedInViewModel,
                                          LoginViewModel loginViewModel,
                                          LogoutUserDataAccessInterface userDataAccessObject) {

        LogoutOutputBoundary presenter = new LogoutPresenter(viewManagerModel, loggedInViewModel, loginViewModel);
        LogoutInputBoundary interactor = new LogoutInteractor(userDataAccessObject, presenter);
        return new LogoutController(interactor);
    }
}
