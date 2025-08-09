package data_access;

import entity.User;

import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * A fake in-memory DAO used only for unit tests. No external I/O.
 */
public class InMemoryUserDataAccessObject implements
        LoginUserDataAccessInterface,
        SignupUserDataAccessInterface,
        LogoutUserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();
    private String currentUser;

    public InMemoryUserDataAccessObject() {
        this.currentUser = null;
    }

    @Override
    public void save(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }

    @Override
    public boolean isUsernameValid(String username) {
        return !existsByName(username);
    }

    @Override
    public void setCurrentUser(String username) {
        this.currentUser = username;
    }

    @Override
    public String getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUsername(String username) {
        this.currentUser = username;
    }

    @Override
    public String getCurrentUsername() {
        return currentUser;
    }
}
