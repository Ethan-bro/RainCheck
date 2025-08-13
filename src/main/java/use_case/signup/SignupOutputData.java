package use_case.signup;

/**
 * Output Data for the Signup Use Case.
 */
public class SignupOutputData {

    private final String username;

    private final boolean useCaseFailed;

    /**
     * Constructs a SignupOutputData object.
     *
     * @param username the username of the newly signed up user
     * @param useCaseFailed whether the signup use case failed
     */
    public SignupOutputData(String username, boolean useCaseFailed) {
        this.username = username;
        this.useCaseFailed = useCaseFailed;
    }

    /**
     * Gets the username of the newly signed up user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Indicates whether the signup use case failed.
     *
     * @return true if the use case failed, false otherwise
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
