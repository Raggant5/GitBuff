package use_case.login;

import entity.User;
import use_case.DataAccessException;
import use_case.EventPublisher;
import use_case.session.UserLoggedInEvent;

/**
 * Interactor implementing business logic for the Login Use Case.
 */
public class LoginInteractor implements LoginInputBoundary {

    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;
    private final EventPublisher<UserLoggedInEvent> userSessionEventPublisher;

    /**
     * Constructs a LoginInteractor instance.
     *
     * @param userDataAccessInterface user data access persistence object
     * @param loginOutputBoundary output boundary presenter
     * @param userSessionEventPublisher publisher notified once login succeeds
     */
    public LoginInteractor(final LoginUserDataAccessInterface userDataAccessInterface,
                           final LoginOutputBoundary loginOutputBoundary,
                           final EventPublisher<UserLoggedInEvent> userSessionEventPublisher) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
        this.userSessionEventPublisher = userSessionEventPublisher;
    }

    @Override
    public void execute(final LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        try {
            if (!userDataAccessObject.existsByName(username)) {
                loginPresenter.prepareFailView(
                        username + ": Account does not exist."
                );
            }
            else {
                final User user = userDataAccessObject.get(username);

                if (!password.equals(user.getPassword())) {
                    loginPresenter.prepareFailView(
                            "Incorrect password for \"" + username + "\"."
                    );
                }
                else {
                    userDataAccessObject.setCurrentUsername(username);

                    final LoginOutputData loginOutputData = new LoginOutputData(
                            user.getName(),
                            user.getHeight(),
                            user.getWeight(),
                            user.getActivityLevel(),
                            user.getGoal(),
                            user.getProfilePicturePath(),
                            user.getDateOfBirth(),
                            user.getGender(),
                            user.getBio(),
                            user.getPreferredUnitSystem(),
                            user.getEquipment(),
                            user.getDietaryRestrictions(),
                            user.getPreferredWorkoutDays(),
                            user.getPreferredWorkoutDurationMinutes(),
                            user.getPrivacySettings(),
                            false
                    );

                    this.userSessionEventPublisher.publish(new UserLoggedInEvent(loginOutputData));
                    loginPresenter.prepareSuccessView(loginOutputData);
                }
            }
        }
        catch (final DataAccessException exception) {
            loginPresenter.prepareFailView("Unable to log in right now. Please try again.");
        }
    }

    @Override
    public void switchToSignupView() {
        this.loginPresenter.switchToSignupView();
    }
}
