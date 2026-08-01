package use_case.login;

import java.util.List;

import entity.Meal;
import entity.User;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {

    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;
    private final ViewMealDataAccessInterface mealsDataAccessObject;

    public LoginInteractor(
            LoginUserDataAccessInterface userDataAccessInterface,
            LoginOutputBoundary loginOutputBoundary,
            ViewMealDataAccessInterface mealsDataAccessObject) {

        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
        this.mealsDataAccessObject = mealsDataAccessObject;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView(
                    username + ": Account does not exist."
            );
            return;
        }

        final User user = userDataAccessObject.get(username);

        if (!password.equals(user.getPassword())) {
            loginPresenter.prepareFailView(
                    "Incorrect password for \"" + username + "\"."
            );
            return;
        }

        final List<Meal> meals =
                mealsDataAccessObject.getMealsForUser(user.getName());

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
                meals,
                false
        );

        loginPresenter.prepareSuccessView(loginOutputData);
    }

    @Override
    public void switchToSignupView() {
        loginPresenter.switchToSignupView();
    }
}