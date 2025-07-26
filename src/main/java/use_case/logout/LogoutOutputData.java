package use_case.logout;

/**
 * Output Data for the Logout Use Case.
 */
public class LogoutOutputData {

    private String username;
    private boolean useCaseFailed;

    /**
     * Constructor LogoutOutputData with username and whether the use case failed.     *
     * @param username       the username of the user logging out
     * @param useCaseFailed  whether the use case failed
     */
    public LogoutOutputData(String username, boolean useCaseFailed) {
        this.username = username;
        this.useCaseFailed = useCaseFailed;
    }

    public String getUsername() {
        return username;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
