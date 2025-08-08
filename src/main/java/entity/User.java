package entity;

/**
 * Interface representing a User entity.
 */
public interface User {

    /**
     * Gets the user's name.
     * @return the user's name
     */
    String getName();

    /**
     * Gets the user's password.
     * @return the user's password
     */
    String getPassword();

    /**
     * Gets the user's email.
     * @return the user's email
     */
    String getEmail();
}
