package use_case.logout;

/**
 * Input Data for the Logout Use Case.
 */
public class LogoutInputData {

    private final String username;

    /**
     * Constructs a LogoutInputData instance.
     *
     * @param username user username
     */
    public LogoutInputData(final String username) {
        this.username = username;
    }

    /**
     * Gets the username.
     *
     * @return username string
     */
    public String getUsername() {
        return this.username;
    }
}
