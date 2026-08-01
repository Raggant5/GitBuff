package use_case.logout;

/**
 * The Logout Interactor implementing business rules for logging out users.
 */
public class LogoutInteractor implements LogoutInputBoundary {

    private final LogoutUserDataAccessInterface userDataAccessObject;
    private final LogoutOutputBoundary logoutPresenter;

    /**
     * Constructs a LogoutInteractor instance.
     *
     * @param userDataAccessInterface user data access persistence object
     * @param logoutOutputBoundary output boundary presenter
     */
    public LogoutInteractor(final LogoutUserDataAccessInterface userDataAccessInterface,
                            final LogoutOutputBoundary logoutOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.logoutPresenter = logoutOutputBoundary;
    }

    @Override
    public void execute(final LogoutInputData logoutInputData) {
        final String username = logoutInputData.getUsername();
        this.userDataAccessObject.setCurrentUsername(null);

        final LogoutOutputData logoutOutputData = new LogoutOutputData(username, false);
        this.logoutPresenter.prepareSuccessView(logoutOutputData);
    }
}

