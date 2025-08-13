package interface_adapter.logout;

import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInputData;

/**
 * The controller for the Logout Use Case.
 */
public class LogoutController {

    private final LogoutInputBoundary logoutUseCaseInteractor;

    /**
     * Constructs a LogoutController with the given use case interactor.
     *
     * @param logoutUseCaseInteractor the interactor for the logout use case
     */
    public LogoutController(LogoutInputBoundary logoutUseCaseInteractor) {
        this.logoutUseCaseInteractor = logoutUseCaseInteractor;
    }

    /**
     * Executes the logout use case for the specified user.
     *
     * @param username the username of the user logging out
     */
    public void execute(String username) {
        final LogoutInputData inputData = new LogoutInputData(username);
        logoutUseCaseInteractor.execute(inputData);
    }
}
