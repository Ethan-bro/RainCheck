package entity;

/**
 * Factory for creating CommonUser objects.
 */
public class CommonUserFactory implements UserFactory {

    /**
     * Creates a new CommonUser with the given name, password, and email.
     * @param name the user's name
     * @param password the user's password
     * @param email the user's email
     * @return a new CommonUser instance
     */
    @Override
    public User create(String name, String password, String email) {
        return new CommonUser(name, password, email);
    }
}
