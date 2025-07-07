package services;

/**
 * Interface for password hashing services.
 */
public interface PasswordHashingService {
    /**
     * Hashes a raw password.
     *
     * @param password the plain text password
     * @return the hashed password
     */
    String hash(String password);

    /**
     * Verifies a raw password against a hashed password.
     *
     * @param rawPassword the plain text password
     * @param hashedPassword the hashed password from storage
     * @return true if they match, false otherwise
     */
    boolean verify(String rawPassword, String hashedPassword);
}
