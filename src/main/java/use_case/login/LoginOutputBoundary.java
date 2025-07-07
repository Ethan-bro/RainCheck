package use_case.login;

/**
 * Output boundary interface for the Login use case.
 * Defines how the output of the login attempt should be presented,
 * without depending on how the UI actually displays it.
 */
public interface LoginOutputBoundary {

    /**
     * Prepares the view when login is successful.
     * @param outputData Data containing user login info.
     */
    void prepareSuccessView(LoginOutputData outputData);

    /**
     * Prepares the view when login fails.
     * @param errorMessage A message describing why login failed (e.g., wrong password).
     */
    void prepareFailView(String errorMessage);
}