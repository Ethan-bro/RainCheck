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

/**
 * Factory class to create instances related to Logout use case.
 */
public final class LogoutUseCaseFactory {

    private LogoutUseCaseFactory() {
        // private constructor to prevent instantiation
    }

    /**
     * Creates a LogoutController with all required dependencies.
     *
     * @param viewManagerModel ViewManagerModel instance
     * @param loggedInViewModel LoggedInViewModel instance
     * @param loginViewModel LoginViewModel instance
     * @param userDataAccessObject Data access interface for logout
     * @return LogoutController instance
     */
    public static LogoutController create(
            final ViewManagerModel viewManagerModel,
            final LoggedInViewModel loggedInViewModel,
            final LoginViewModel loginViewModel,
            final LogoutUserDataAccessInterface userDataAccessObject
    ) {
        final LogoutOutputBoundary presenter = new LogoutPresenter(viewManagerModel, loggedInViewModel, loginViewModel);
        final LogoutInputBoundary interactor = new LogoutInteractor(userDataAccessObject, presenter);
        return new LogoutController(interactor);
    }
}
