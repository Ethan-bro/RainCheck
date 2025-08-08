package interface_adapter.login;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

/**
 * The controller for the Login Use Case.
 */
public class LoginController {

    private final LoginInputBoundary loginUseCaseInteractor;

    public LoginController(LoginInputBoundary loginUseCaseInteractor) {
        this.loginUseCaseInteractor = loginUseCaseInteractor;
    }

    /**
     * Executes the Login Use Case.
     * @param username the username of the user logging in
     * @param password the password of the user logging in
     * @param email the email of the user logging in
     */
    public void execute(String username, String password, String email) {
        final LoginInputData loginInputData = new LoginInputData(
                username, password, email);

        loginUseCaseInteractor.execute(loginInputData);
    }

    /**
     * Switch to signup view method.
     */
    public void switchToSignupView() {
        loginUseCaseInteractor.switchToSignupView();
    }
}
