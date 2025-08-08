package interface_adapter.login;

/**
 * Represents the state for the Login View Model,
 * holding user credentials and error messages.
 */
public class LoginState {

    private String email = "";
    private String emailError;
    private String username = "";
    private String loginError;
    private String password = "";

    /**
     * Gets the email address entered by the user.
     *
     * @return the email address as a String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the username entered by the user.
     *
     * @return the username as a String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the login error message.
     *
     * @return the login error message as a String, or null if none
     */
    public String getLoginError() {
        return loginError;
    }

    /**
     * Gets the password entered by the user.
     *
     * @return the password as a String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the email error message.
     *
     * @param emailError the error message related to the email input
     */
    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    /**
     * Sets the username.
     *
     * @param username the username string to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the login error message.
     *
     * @param loginError the error message related to login
     */
    public void setLoginError(String loginError) {
        this.loginError = loginError;
    }

    /**
     * Sets the password.
     *
     * @param password the password string to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
