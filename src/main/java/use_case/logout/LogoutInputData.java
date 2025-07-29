package use_case.logout;

/**
 * The Input Data for the Logout Use Case.
 */
public class LogoutInputData {

    private final String username;

    /**
     * Constructor for LogoutInputData with a username.
     * @param username the username of the user logging out
     */
    public LogoutInputData(String username) {
        this.username = username;
    }

    /**
     * This method gets the user's username.
     * @return the username of the user logging out
     */
    public String getUsername() {
        return username;
    }
}
