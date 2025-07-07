package entity;

/**
 * A simple implementation of the User interface.
 */
public class CommonUser implements User {

    private final String name;
    private final String passwordHash;

    public CommonUser(String name, String passwordHash) {
        this.name = name;
        this.passwordHash = passwordHash;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPasswordHash() {
        return passwordHash;
    }
}
