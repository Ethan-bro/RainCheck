package use_case.login;

/**
 * Input boundary interface for the Login use case.
 * This interface defines the method required to initiate a login attempt.
 */
public interface LoginInputBoundary {

    /**
     * Attempts to log a user in using the given input data.
     * @param loginInputData An object containing the user's login credentials.
     */
    void execute(LoginInputData loginInputData);

    /**
     * Switches to the signup view.
     */
    void switchToSignupView();

}
