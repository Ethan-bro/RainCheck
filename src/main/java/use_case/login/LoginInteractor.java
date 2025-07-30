package use_case.login;

import entity.User;

/**
 * The Login Interactor handles user login use case logic.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
            return;
        }

        User user = userDataAccessObject.get(username);
        String storedPassword = user.getPassword();
        String email = user.getEmail();

        if (!password.equals(storedPassword)) {
            loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
        } else {
            userDataAccessObject.setCurrentUser(username);
            LoginOutputData outputData = new LoginOutputData(username, email, false);
            loginPresenter.prepareSuccessView(outputData);
        }
    }

    @Override
    public void switchToSignupView() {
        loginPresenter.switchToSignupView();
    }
}
