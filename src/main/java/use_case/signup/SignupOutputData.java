package use_case.signup;

/**
 * Output Data for the Signup Use Case.
 */
public class SignupOutputData {

    private final String username;
    private final boolean useCaseFailed;

    /**
     * Constructs a SignupOutputData instance.
     *
     * @param username user username
     * @param useCaseFailed execution failure flag
     */
    public SignupOutputData(final String username, final boolean useCaseFailed) {
        this.username = username;
        this.useCaseFailed = useCaseFailed;
    }

    /**
     * Gets the username.
     *
     * @return username string
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the use case failure flag status.
     *
     * @return true if execution failed; false otherwise
     */
    public boolean isUseCaseFailed() {
        return this.useCaseFailed;
    }
}
