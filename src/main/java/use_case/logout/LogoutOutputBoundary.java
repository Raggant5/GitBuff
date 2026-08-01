package use_case.logout;

/**
 * The output boundary for the Logout Use Case.
 */
public interface LogoutOutputBoundary {

    /**
     * Prepares the success view for the Logout Use Case.
     *
     * @param outputData output data containing user details
     */
    void prepareSuccessView(LogoutOutputData outputData);

    /**
     * Prepares the failure view for the Logout Use Case.
     *
     * @param errorMessage explanation of failure cause
     */
    void prepareFailView(String errorMessage);
}
