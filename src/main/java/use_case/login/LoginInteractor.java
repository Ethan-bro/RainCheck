package use_case.login;

import entity.User;
import services.PasswordHashingService;

/**
 * The Login Interactor handles user login use case logic.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;
    private final PasswordHashingService passwordHashingService;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary,
                           PasswordHashingService passwordHashingService) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
        this.passwordHashingService = passwordHashingService;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
        } else {
            User user = userDataAccessObject.get(username);
            String storedHash = user.getPasswordHash();

            if (!passwordHashingService.verify(password, storedHash)) {
                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            } else {
                userDataAccessObject.setCurrentUser(username);
                LoginOutputData outputData = new LoginOutputData(username, false);
                loginPresenter.prepareSuccessView(outputData);
            }
        }
    }

    @Override
    public void switchToSignupView() {
        loginPresenter.switchToSignupView();
    }
}