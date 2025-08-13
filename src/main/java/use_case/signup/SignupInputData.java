package use_case.signup;

/**
 * The Input Data for the Signup Use Case.
 */
public class SignupInputData {

    private final String username;
    private final String password;
    private final String repeatPassword;
    private final String email;

    /**
     * Constructs a SignupInputData object.
     *
     * @param username the username for the new account
     * @param password the password for the new account
     * @param repeatPassword the repeated password for confirmation
     * @param email the email address for the new account
     */
    public SignupInputData(String username, String password, String repeatPassword, String email) {
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.email = email;
    }

    /**
     * Gets the username for the new account.
     *
     * @return the username
     */
    String getUsername() {
        return username;
    }

    /**
     * Gets the password for the new account.
     *
     * @return the password
     */
    String getPassword() {
        return password;
    }

    /**
     * Gets the repeated password for confirmation.
     *
     * @return the repeated password
     */
    public String getRepeatPassword() {
        return repeatPassword;
    }

    /**
     * Gets the email address for the new account.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }
}
