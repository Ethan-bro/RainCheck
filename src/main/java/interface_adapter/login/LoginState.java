package interface_adapter.login;

/**
 * The state for the Login View Model.
 */
public class LoginState {
    private String email = "";
    private String emailError;
    private String username = "";
    private String loginError;
    private String password = "";

    public String getEmail() { return email; }

    public String getUsername() {
        return username;
    }

    public String getLoginError() {
        return loginError;
    }

    public String getPassword() {
        return password;
    }

    public void setEmailError(String emailError) { this.emailError = emailError; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLoginError(String usernameError) {
        this.loginError = usernameError;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}