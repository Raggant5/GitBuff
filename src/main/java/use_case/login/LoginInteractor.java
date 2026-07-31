package use_case.login;

import entity.User;

/**
 * Interactor implementing business logic for the Login Use Case.
 */
public class LoginInteractor implements LoginInputBoundary {

    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    /**
     * Constructs a LoginInteractor instance.
     *
     * @param userDataAccessInterface user data access persistence object
     * @param loginOutputBoundary output boundary presenter
     */
    public LoginInteractor(final LoginUserDataAccessInterface userDataAccessInterface,
                           final LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(final LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if (!this.userDataAccessObject.existsByName(username)) {
            this.loginPresenter.prepareFailView(username + ": Account does not exist.");
        }
        else {
            final String pwd = this.userDataAccessObject.get(username).getPassword();
            if (!password.equals(pwd)) {
                this.loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            }
            else {
                final User user = this.userDataAccessObject.get(username);
                this.userDataAccessObject.setCurrentUsername(username);

                final LoginOutputData loginOutputData = new LoginOutputData(
                        user.getName(),
                        user.getHeight(),
                        user.getWeight(),
                        user.getActivityLevel(),
                        user.getGoal(),
                        user.getProfilePicturePath(),
                        false
                );
                this.loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }

    @Override
    public void switchToSignupView() {
        this.loginPresenter.switchToSignupView();
    }
}
