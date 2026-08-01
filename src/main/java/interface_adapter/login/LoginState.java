package interface_adapter.login;

/**
 * The state for the Login View Model.
 */
public class LoginState {

    private String username = "";
    private String loginError;
    private String password = "";

    public String getUsername() {
        return this.username;
    }

    public String getLoginError() {
        return this.loginError;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setLoginError(final String loginError) {
        this.loginError = loginError;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
