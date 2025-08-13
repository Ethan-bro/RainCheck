package use_case.logout;

/**
 * The Logout Interactor.
 */
public class LogoutInteractor implements LogoutInputBoundary {
    private final LogoutUserDataAccessInterface userDataAccessObject;
    private final LogoutOutputBoundary logoutPresenter;

    /**
     * Constructs a LogoutInteractor.
     *
     * @param userDataAccessObject the data access interface for user data
     * @param logoutPresenter the output boundary to present results
     */
    public LogoutInteractor(LogoutUserDataAccessInterface userDataAccessObject,
                            LogoutOutputBoundary logoutPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.logoutPresenter = logoutPresenter;
    }

    /**
     * Executes the use case for user logout.
     *
     * @param logoutInputData the input data containing username
     */
    @Override
    public void execute(LogoutInputData logoutInputData) {
        final String username = logoutInputData.getUsername();
        userDataAccessObject.setCurrentUsername(null);

        final LogoutOutputData logoutOutputData = new LogoutOutputData(username, false);
        logoutPresenter.prepareSuccessView(logoutOutputData);
    }
}
