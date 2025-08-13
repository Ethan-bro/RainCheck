package entity;

/**
 * A simple implementation of the User interface.
 */
public class CommonUser implements User {

    private final String name;
    private final String password;
    private final String email;

    /**
     * Constructs a CommonUser with the given name, password, and email.
     * @param name the user's name
     * @param password the user's password
     * @param email the user's email
     */
    public CommonUser(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    /**
     * Gets the user's name.
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the user's password.
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user's email.
     * @return the email
     */
    @Override
    public String getEmail() {
        return email;
    }
}
