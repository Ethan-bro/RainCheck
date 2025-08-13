package interface_adapter.signup;

/**
 * The state for the Signup View Model.
 */
public class SignupState {
    private String email = "";
    private String emailError;
    private String username = "";
    private String usernameError;
    private String password = "";
    private String passwordError;
    private String repeatPassword = "";
    private String repeatPasswordError;

    /**
     * Returns the email address entered by the user.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the username entered by the user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the username error message, if any.
     *
     * @return the username error message
     */
    public String getUsernameError() {
        return usernameError;
    }

    /**
     * Returns the password entered by the user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the password error message, if any.
     *
     * @return the password error message
     */
    public String getPasswordError() {
        return passwordError;
    }

    /**
     * Returns the repeated password entered by the user.
     *
     * @return the repeated password
     */
    public String getRepeatPassword() {
        return repeatPassword;
    }

    /**
     * Returns the repeated password error message, if any.
     *
     * @return the repeated password error message
     */
    public String getRepeatPasswordError() {
        return repeatPasswordError;
    }

    /**
     * Sets the email address.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the email error message.
     *
     * @param emailError the error message to set
     */
    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    /**
     * Sets the username.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the username error message.
     *
     * @param usernameError the error message to set
     */
    public void setUsernameError(String usernameError) {
        this.usernameError = usernameError;
    }

    /**
     * Sets the password.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the password error message.
     *
     * @param passwordError the error message to set
     */
    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    /**
     * Sets the repeated password.
     *
     * @param repeatPassword the repeated password to set
     */
    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    /**
     * Sets the repeated password error message.
     *
     * @param repeatPasswordError the error message to set
     */
    public void setRepeatPasswordError(String repeatPasswordError) {
        this.repeatPasswordError = repeatPasswordError;
    }

    /**
     * Returns a string representation of the SignupState, including username, password, and repeatPassword fields.
     *
     * @return a string representation of the SignupState
     */
    @Override
    public String toString() {
        return "SignupState{"
                + "username='" + username + '\''
                + ", password='" + password + '\''
                + ", repeatPassword='" + repeatPassword + '\''
                + '}';
    }
}
