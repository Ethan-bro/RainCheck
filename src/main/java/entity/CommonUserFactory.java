package entity;

/**
 * Factory for creating CommonUser objects.
 */
public class CommonUserFactory implements UserFactory {

    @Override
    public User create(String name, String passwordhash) {
        return new CommonUser(name, passwordhash);
    }
}