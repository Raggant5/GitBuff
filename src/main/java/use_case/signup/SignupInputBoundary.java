package use_case.signup;

/**
 * Input Boundary for actions related to signing up.
 */
public interface SignupInputBoundary {

    /**
     * Executes the signup use case.
     *
     * @param signupInputData input data containing credentials
     */
    void execute(SignupInputData signupInputData);

    /**
     * Executes the switch view request to navigate to LoginView with credentials.
     *
     * @param username the entered username to transfer
     * @param password the entered password to transfer
     */
    void switchToLoginView(String username, String password);
}
