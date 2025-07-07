package use_case.login;

/**
 * Input data object for the Login use case.
 * Holds the user's login credentials to be passed to the interactor.
 */
public class LoginInputData {
    private final String username;
    private final String password;

    /**
     * Constructs a new LoginInputData object.
     * @param username The user's entered username.
     * @param password The user's entered password (in plain text).
     */
    public LoginInputData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return The entered username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The entered password.
     */
    public String getPassword() {
        return password;
    }
}