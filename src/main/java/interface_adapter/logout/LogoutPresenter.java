package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

/**
 * The Presenter for the Logout Use Case.
 */
public class LogoutPresenter implements LogoutOutputBoundary {

    private LoggedInViewModel loggedInViewModel;
    private ViewManagerModel viewManagerModel;
    private LoginViewModel loginViewModel;


    /**
     * Constructs a LogoutPresenter with the required view models.
     *
     * @param viewManagerModel the view manager model
     * @param loggedInViewModel the view model for the logged-in state
     * @param loginViewModel the view model for the login state
     */
    public LogoutPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
    }


    /**
     * Prepares the view for a successful logout.
     * Clears user state and switches to the login view.
     *
     * @param response the output data from the logout use case
     */
    @Override
    public void prepareSuccessView(LogoutOutputData response) {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername("");
        loggedInViewModel.setState(loggedInState);
        loggedInViewModel.firePropertyChanged();

        final LoginState loginState = loginViewModel.getState();
        loginState.setPassword("");
        loginState.setUsername("");
        loginViewModel.setState(loginState);
        loginViewModel.firePropertyChanged();

        // Switch to the LoginView.
        this.viewManagerModel.setState(loginViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }


    /**
     * Prepares the view for a failed logout attempt. (No-op, as logout cannot fail.)
     *
     * @param error the error message (unused)
     */
    @Override
    public void prepareFailView(String error) {
        // We can safely assume that logout can't fail.
    }
}
