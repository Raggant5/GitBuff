package use_case.signup;

/**
 * The Input Data for the Signup Use Case.
 */
public class SignupInputData {

    private final String username;
    private final String password;
    private final String repeatPassword;

    /**
     * Constructs a SignupInputData instance.
     *
     * @param username user username
     * @param password user password
     * @param repeatPassword repeated password confirmation
     */
    public SignupInputData(final String username, final String password, final String repeatPassword) {
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getRepeatPassword() {
        return this.repeatPassword;
    }
}
