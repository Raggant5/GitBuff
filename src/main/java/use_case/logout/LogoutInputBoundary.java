package use_case.logout;

/**
 * Input Boundary for actions related to logging out.
 */
public interface LogoutInputBoundary {

    /**
     * Executes the logout use case.
     *
     * @param logoutInputData input data containing current session details
     */
    void execute(LogoutInputData logoutInputData);
}
