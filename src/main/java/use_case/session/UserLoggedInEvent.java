package use_case.session;

import use_case.login.LoginOutputData;

/**
 * Event published once a user has successfully logged in.
 */
public class UserLoggedInEvent {

    private final LoginOutputData data;

    public UserLoggedInEvent(final LoginOutputData data) {
        this.data = data;
    }

    public LoginOutputData getData() {
        return this.data;
    }
}
