package interface_adapter.logged_in;

import java.util.List;

import entity.Task;

/**
 * The State information representing the logged-in user.
 */
public class LoggedInState {
    private String username = "";
    private String email = "";
    private String password = "";
    private String passwordError;
    private List<Task> weekTasks = List.of();


    /**
     * Copy constructor for LoggedInState.
     *
     * @param copy the LoggedInState to copy from
     */
    public LoggedInState(LoggedInState copy) {
        username = copy.username;
        email = copy.email;
        password = copy.password;
        passwordError = copy.passwordError;
        weekTasks = copy.weekTasks;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.

    /**
     * Default constructor for LoggedInState.
     */
    public LoggedInState() {
    }


    /**
     * Returns the username of the logged-in user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }


    /**
     * Sets the username of the logged-in user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * Returns the email of the logged-in user.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }


    /**
     * Sets the email of the logged-in user.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Sets the password of the logged-in user.
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
     * Returns the password of the logged-in user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }


    /**
     * Returns the list of tasks for the current week.
     *
     * @return the list of tasks for the week
     */
    public List<Task> getWeekTasks() {
        return weekTasks;
    }

    public void setWeekTasks(List<Task> weekTasks) {
        this.weekTasks = weekTasks;
    }
}
