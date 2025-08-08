package use_case.login;

/**
 * Input data object for the Login use case.
 * Holds the user's login credentials to be passed to the interactor.
 */
public class LoginInputData {
    private final String username;
    private final String password;
    private final String email;

    /**
     * Constructs a new LoginInputData object.
     *
     * @param username The user's entered username.
     * @param password The user's entered password (in plain text).
     * @param email    The user's entered email.
     */
    public LoginInputData(
            String username,
            String password,
            String email
    ) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * Returns the entered username.
     *
     * @return The entered username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the entered password.
     *
     * @return The entered password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the entered email.
     *
     * @return The entered email.
     */
    public String getEmail() {
        return email;
    }
}
