package use_case.login;

/**
 * Interface for hashing and verifying passwords.
 */
public interface PasswordHashingService {
    /**
     * Hashes a raw password into a secure hash.
     * @param password the raw password
     * @return the hashed password
     */
    String hash(String password);

    /**
     * Verifies that a raw password matches a stored hash.
     * @param rawPassword the raw password input
     * @param hashedPassword the stored password hash
     * @return true if they match; false otherwise
     */
    boolean verify(String rawPassword, String hashedPassword);
}