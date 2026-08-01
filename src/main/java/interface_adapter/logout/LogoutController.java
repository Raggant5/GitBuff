package interface_adapter.logout;

import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInputData;

/**
 * The controller for the Logout Use Case.
 */
public class LogoutController {

    private final LogoutInputBoundary logoutUseCaseInteractor;

    /**
     * Constructs a LogoutController instance.
     *
     * @param logoutUseCaseInteractor interactor boundary for executing logout logic
     */
    public LogoutController(final LogoutInputBoundary logoutUseCaseInteractor) {
        this.logoutUseCaseInteractor = logoutUseCaseInteractor;
    }

    /**
     * Executes the Logout Use Case.
     *
     * @param username the username of the user logging out
     */
    public void execute(final String username) {
        final LogoutInputData logoutInputData = new LogoutInputData(username);
        this.logoutUseCaseInteractor.execute(logoutInputData);
    }
}
