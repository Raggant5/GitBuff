package interface_adapter.login;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

/**
 * The controller for the Login Use Case.
 */
public class LoginController {

    private final LoginInputBoundary loginUseCaseInteractor;

    /**
     * Constructs a LoginController instance.
     *
     * @param loginUseCaseInteractor interactor boundary for executing login logic
     */
    public LoginController(final LoginInputBoundary loginUseCaseInteractor) {
        this.loginUseCaseInteractor = loginUseCaseInteractor;
    }

    /**
     * Executes the Login Use Case.
     *
     * @param username the username of the user logging in
     * @param password the password of the user logging in
     */
    public void execute(final String username, final String password) {
        final LoginInputData loginInputData = new LoginInputData(username, password);
        this.loginUseCaseInteractor.execute(loginInputData);
    }

    /**
     * Executes the switch view request to navigate to SignupView.
     */
    public void switchToSignupView() {
        this.loginUseCaseInteractor.switchToSignupView();
    }
}
