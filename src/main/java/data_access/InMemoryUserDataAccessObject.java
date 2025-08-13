package data_access;

import java.util.HashMap;
import java.util.Map;

import entity.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * A fake in-memory DAO used only for unit tests. No external I/O.
 */
public class InMemoryUserDataAccessObject implements
        LoginUserDataAccessInterface,
        SignupUserDataAccessInterface,
        LogoutUserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();
    private String currentUser;

    /**
     * Constructs an in-memory user data access object.
     */
    public InMemoryUserDataAccessObject() {
        this.currentUser = null;
    }

    @Override
    /**
     * Saves a user to the in-memory store.
     * @param user the user to save
     */
    public void save(User user) {
        users.put(user.getName(), user);
    }

    @Override
    /**
     * Gets a user by username.
     * @param username the username
     * @return the user, or null if not found
     */
    public User get(String username) {
        return users.get(username);
    }

    @Override
    /**
     * Checks if a user exists by username.
     * @param username the username
     * @return true if the user exists, false otherwise
     */
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }

    @Override
    /**
     * Checks if a username is valid (not already taken).
     * @param username the username to check
     * @return true if valid, false otherwise
     */
    public boolean isUsernameValid(String username) {
        return !existsByName(username);
    }

    @Override
    /**
     * Sets the current user by username.
     * @param username the username to set as current
     */
    public void setCurrentUser(String username) {
        this.currentUser = username;
    }

    @Override
    /**
     * Gets the current user.
     * @return the current user
     */
    public String getCurrentUser() {
        return currentUser;
    }

    @Override
    /**
     * Sets the current username.
     * @param username the username to set as current
     */
    public void setCurrentUsername(String username) {
        this.currentUser = username;
    }

    @Override
    /**
     * Gets the current username.
     * @return the current username
     */
    public String getCurrentUsername() {
        return currentUser;
    }
}
