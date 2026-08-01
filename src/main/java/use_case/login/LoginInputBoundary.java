package use_case.login;

/**
 * Input Boundary for actions related to logging in.
 */
public interface LoginInputBoundary {

    /**
     * Executes the login use case.
     *
     * @param loginInputData input data containing credentials
     */
    void execute(LoginInputData loginInputData);

    /**
     * Triggers navigation switch to the signup view.
     */
    void switchToSignupView();
}
