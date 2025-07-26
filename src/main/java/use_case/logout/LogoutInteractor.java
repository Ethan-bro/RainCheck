package use_case.logout;

/**
 * The Logout Interactor.
 */
public class LogoutInteractor implements LogoutInputBoundary {
    private final LogoutUserDataAccessInterface userDataAccessObject;
    private final LogoutOutputBoundary logoutPresenter;

    public LogoutInteractor(LogoutUserDataAccessInterface userDataAccessObject,
                            LogoutOutputBoundary logoutPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.logoutPresenter = logoutPresenter;
    }

    @Override
    public void execute(LogoutInputData logoutInputData) {
        String username = logoutInputData.getUsername();
        userDataAccessObject.setCurrentUsername(null);

        LogoutOutputData logoutOutputData = new LogoutOutputData(username, false);
        logoutPresenter.prepareSuccessView(logoutOutputData);
    }
}
