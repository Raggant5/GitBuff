package interface_adapter.login;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class LoginViewModel extends ViewModel<LoginState> {

    /**
     * Constructs a LoginViewModel instance with default initial state.
     */
    public LoginViewModel() {
        super("log in");
        setState(new LoginState());
    }
}
