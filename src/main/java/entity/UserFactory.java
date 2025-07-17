package entity;

/**
 * Factory interface for creating User entities.
 */
public interface UserFactory {
    /**
     * Creates a new User.
     *
     * @param username The username.
     * @param passwordHash The already-hashed password.
     * @return A new User.
     */
    User create(String username, String passwordHash);
}