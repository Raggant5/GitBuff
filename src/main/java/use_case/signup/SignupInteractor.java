package use_case.signup;

import entity.User;
import entity.UserFactory;
import use_case.DataAccessException;

/**
 * Interactor implementing business rules for the Signup Use Case.
 */
public class SignupInteractor implements SignupInputBoundary {

    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    /**
     * Constructs a SignupInteractor instance.
     *
     * @param signupDataAccessInterface user data access persistence object
     * @param signupOutputBoundary output boundary presenter
     * @param userFactory factory interface for instantiating users
     */
    public SignupInteractor(final SignupUserDataAccessInterface signupDataAccessInterface,
                            final SignupOutputBoundary signupOutputBoundary,
                            final UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(final SignupInputData signupInputData) {
        try {
            if (this.userDataAccessObject.existsByName(signupInputData.getUsername())) {
                this.userPresenter.prepareFailView("User already exists.");
            }
            else if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())) {
                this.userPresenter.prepareFailView("Passwords don't match.");
            }
            else {
                final User user =
                        this.userFactory.create(signupInputData.getUsername(), signupInputData.getPassword());
                this.userDataAccessObject.save(user);
                this.userDataAccessObject.setCurrentUsername(user.getName());

                final SignupOutputData signupOutputData = new SignupOutputData(user.getName(), false);
                this.userPresenter.prepareSuccessView(signupOutputData);
            }
        }
        catch (final DataAccessException exception) {
            this.userPresenter.prepareFailView("Unable to sign up right now. Please try again.");
        }
    }

    @Override
    public void switchToLoginView(final String username, final String password) {
        this.userPresenter.switchToLoginView(username, password);
    }
}


