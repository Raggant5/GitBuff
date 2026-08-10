package interface_adapter.login;

/**
 * The state for the Login View Model.
 */
public class LoginState {

    private String username = "";
    private String loginError;
    private String password = "";

    /**
     * Gets the username.
     *
     * @return username string.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the login error.
     *
     * @return login error string.
     */
    public String getLoginError() {
        return this.loginError;
    }

    /**
     * Gets the password.
     *
     * @return password string.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the username.
     *
     * @param username username string.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Sets the login error message.
     *
     * @param loginError error message string.
     */
    public void setLoginError(final String loginError) {
        this.loginError = loginError;
    }

    /**
     * Sets the password.
     *
     * @param password password string.
     */
    public void setPassword(final String password) {
        this.password = password;
    }
}
