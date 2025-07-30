package interface_adapter.logged_in;

import entity.Task;
import java.util.List;

/**
 * The State information representing the logged-in user.
 */
public class LoggedInState {
    private String username = "";
    private String email = "";           // ← ADDED
    private String password = "";
    private String passwordError;
    private List<Task> weekTasks = List.of();

    public LoggedInState(LoggedInState copy) {
        username = copy.username;
        email = copy.email;             // ← ADDED
        password = copy.password;
        passwordError = copy.passwordError;
        weekTasks = copy.weekTasks;     // ← Also copy weekTasks
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public LoggedInState() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getPassword() {
        return password;
    }

    public List<Task> getWeekTasks() {
        return weekTasks;
    }

    public void setWeekTasks(List<Task> weekTasks) {
        this.weekTasks = weekTasks;
    }
}
