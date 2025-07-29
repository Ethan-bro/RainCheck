package entity;

/**
 * Factory interface for creating User entities.
 */
public interface UserFactory {
    /**
     * Creates a new User.
     *
     * @param username The username.
     * @param password The password.
     * @return A new User.
     */
    User create(String username, String password);
}