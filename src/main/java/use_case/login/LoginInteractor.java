package use_case.login;

import entity.User;

/**
 * The Login Interactor handles user login use case logic.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    /**
     * Constructs a LoginInteractor.
     *
     * @param userDataAccessInterface the data access interface for user data
     * @param loginOutputBoundary the output boundary to present results
     */
    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    /**
     * Executes the use case for user login.
     *
     * @param loginInputData the input data containing username and password
     */
    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
        }
        else {
            final User user = userDataAccessObject.get(username);
            final String storedPassword = user.getPassword();
            final String email = user.getEmail();

            if (!password.equals(storedPassword)) {
                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            }
            else {
                userDataAccessObject.setCurrentUser(username);
                final LoginOutputData outputData = new LoginOutputData(username, email, false);
                loginPresenter.prepareSuccessView(outputData);
            }
        }
    }

    /**
     * Switches the view to the signup screen.
     */
    @Override
    public void switchToSignupView() {
        loginPresenter.switchToSignupView();
    }
}
