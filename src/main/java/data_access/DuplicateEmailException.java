package data_access;

/**
 * Exception thrown when a duplicate email is encountered during registration or update.
 */
public class DuplicateEmailException extends Exception {

    /**
     * Constructs a DuplicateEmailException with the specified detail message.
     * @param message the detail message
     */
    public DuplicateEmailException(String message) {
        super(message);
    }
}
