package interface_adapter.signup;

/**
 * The state for the Signup View Model.
 */
public class SignupState {

    private String username = "";
    private String usernameError;
    private String password = "";
    private String passwordError;
    private String repeatPassword = "";
    private String repeatPasswordError;

    /**
     * Gets the username.
     *
     * @return username string.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the username error.
     *
     * @return username error string.
     */
    public String getUsernameError() {
        return this.usernameError;
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
     * Gets the password error.
     *
     * @return password error string.
     */
    public String getPasswordError() {
        return this.passwordError;
    }

    /**
     * Gets the repeated password.
     *
     * @return repeated password string.
     */
    public String getRepeatPassword() {
        return this.repeatPassword;
    }

    /**
     * Gets the repeated password error.
     *
     * @return repeated password error string.
     */
    public String getRepeatPasswordError() {
        return this.repeatPasswordError;
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
     * Sets the username error message.
     *
     * @param usernameError username error string.
     */
    public void setUsernameError(final String usernameError) {
        this.usernameError = usernameError;
    }

    /**
     * Sets the password.
     *
     * @param password password string.
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Sets the password error message.
     *
     * @param passwordError password error string.
     */
    public void setPasswordError(final String passwordError) {
        this.passwordError = passwordError;
    }

    /**
     * Sets the repeated password.
     *
     * @param repeatPassword repeated password string.
     */
    public void setRepeatPassword(final String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    /**
     * Sets the repeated password error message.
     *
     * @param repeatPasswordError repeated password error string.
     */
    public void setRepeatPasswordError(final String repeatPasswordError) {
        this.repeatPasswordError = repeatPasswordError;
    }

    @Override
    public String toString() {
        return "SignupState{"
                + "username='" + this.username + '\''
                + ", password='" + this.password + '\''
                + ", repeatPassword='" + this.repeatPassword + '\''
                + '}';
    }
}
