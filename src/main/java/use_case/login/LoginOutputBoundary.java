package use_case.login;

/**
 * Output boundary for the Login Use Case.
 */
public interface LoginOutputBoundary {

    /**
     * Prepares the success view for the Login Use Case.
     *
     * @param outputData output data containing user details
     */
    void prepareSuccessView(LoginOutputData outputData);

    /**
     * Prepares the failure view for the Login Use Case.
     *
     * @param errorMessage explanation of failure cause
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the signup view.
     */
    void switchToSignupView();
}
