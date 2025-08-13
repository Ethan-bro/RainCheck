package entity;

/**
 * Factory interface for creating User entities.
 */
public interface UserFactory {
    /**
     * Creates a new User.
     * @param username the username
     * @param password the password
     * @param email the email
     * @return a new User
     */
    User create(String username, String password, String email);
}
