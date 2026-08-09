package interface_adapter.signup;

import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

/**
 * Controller for the Signup Use Case.
 */
public class SignupController {

    private final SignupInputBoundary userSignupUseCaseInteractor;

    /**
     * Constructs a SignupController instance.
     *
     * @param userSignupUseCaseInteractor interactor boundary for executing signup logic.
     */
    public SignupController(final SignupInputBoundary userSignupUseCaseInteractor) {
        this.userSignupUseCaseInteractor = userSignupUseCaseInteractor;
    }

    /**
     * Executes the Signup Use Case.
     *
     * @param username the username to sign up.
     * @param password1 the password.
     * @param password2 the password repeated.
     */
    public void execute(final String username, final String password1, final String password2) {
        final SignupInputData signupInputData = new SignupInputData(username, password1, password2);
        this.userSignupUseCaseInteractor.execute(signupInputData);
    }

    /**
     * Executes the switch view request to navigate to LoginView with transferred credentials.
     *
     * @param username the entered username to transfer.
     * @param password the entered password to transfer.
     */
    public void switchToLoginView(final String username, final String password) {
        this.userSignupUseCaseInteractor.switchToLoginView(username, password);
    }

    /**
     * Executes the switch view request to navigate to LoginView without parameters.
     */
    public void switchToLoginView() {
        this.userSignupUseCaseInteractor.switchToLoginView("", "");
    }
}
