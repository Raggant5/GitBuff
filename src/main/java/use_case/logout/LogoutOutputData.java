package use_case.logout;

/**
 * Output Data for the Logout Use Case.
 */
public class LogoutOutputData {

    private final String username;
    private final boolean useCaseFailed;

    /**
     * Constructs a LogoutOutputData instance.
     *
     * @param username user username
     * @param useCaseFailed execution failure flag
     */
    public LogoutOutputData(final String username, final boolean useCaseFailed) {
        this.username = username;
        this.useCaseFailed = useCaseFailed;
    }

    /**
     * Gets the username.
     *
     * @return username string
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the use case failure flag status.
     *
     * @return true if execution failed; false otherwise
     */
    public boolean isUseCaseFailed() {
        return this.useCaseFailed;
    }
}
