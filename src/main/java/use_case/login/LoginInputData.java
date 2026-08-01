package use_case.login;

/**
 * Input Data for the Login Use Case.
 */
public class LoginInputData {

    private final String username;
    private final String password;

    /**
     * Constructs a LoginInputData instance.
     *
     * @param username user username
     * @param password user password
     */
    public LoginInputData(final String username, final String password) {
        this.username = username;
        this.password = password;
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
     * Gets the password.
     *
     * @return password string
     */
    public String getPassword() {
        return this.password;
    }
}
