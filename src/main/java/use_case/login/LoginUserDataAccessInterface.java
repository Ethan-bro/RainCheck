package use_case.login;

import entity.User;

/**
 * DAO for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {

    /**
     * Checks if the given username exists.
     * @param username the username to look for
     * @return true if a user with the given username exists; false otherwise
     */
    boolean existsByName(String username);

    /**
     * Saves the user.
     * @param user the user to save
     */
    void save(User user);

    /**
     * Returns the user with the given username.
     * @param username the username to look up
     * @return the user with the given username
     */
    User get(String username);

    /**
     * Sets the current logged-in user's username.
     * @param username the username of the user to mark as currently logged in
     */
    void setCurrentUser(String username);

    /**
     * Returns the username of the currently logged-in user.
     * If no user is currently logged in, this may return null.
     * @return the username of the current user, or null if no user is logged in
     */
    String getCurrentUser();

}