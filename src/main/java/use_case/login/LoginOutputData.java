package use_case.login;

/**
 * Output data object for the Login use case.
 * Sent from the interactor to the presenter after login attempt.
 */
public class LoginOutputData {

    private final String username;
    private final String email;
    private final boolean useCaseFailed;

    /**
     * Constructs a new LoginOutputData object.
     *
     * @param username       The username of the user attempting to log in.
     * @param email          The user's email address.
     * @param useCaseFailed  Indicates whether the login use case failed (e.g., wrong password).
     */
    public LoginOutputData(String username, String email, boolean useCaseFailed) {
        this.username = username;
        this.email = email;
        this.useCaseFailed = useCaseFailed;
    }

    /**
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return true if the login use case failed; false if successful.
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
