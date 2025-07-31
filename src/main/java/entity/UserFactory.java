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
     * @param email The email.
     * @return A new User.
     */
    User create(String username, String password, String email);
}