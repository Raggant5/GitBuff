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

    public String getUsername() {
        return this.username;
    }

    public String getUsernameError() {
        return this.usernameError;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPasswordError() {
        return this.passwordError;
    }

    public String getRepeatPassword() {
        return this.repeatPassword;
    }

    public String getRepeatPasswordError() {
        return this.repeatPasswordError;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setUsernameError(final String usernameError) {
        this.usernameError = usernameError;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setPasswordError(final String passwordError) {
        this.passwordError = passwordError;
    }

    public void setRepeatPassword(final String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

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
