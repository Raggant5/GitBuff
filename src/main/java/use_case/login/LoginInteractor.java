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

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary, ViewMealDataAccessInterface mealsDataAccessObject) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
        this.mealsDataAccessObject = mealsDataAccessObject;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();
        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
        }
        else {
            final String pwd = userDataAccessObject.get(username).getPassword();
            if (!password.equals(pwd)) {
                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            }
            else {

                final User user = userDataAccessObject.get(loginInputData.getUsername());
                final List<Meal> meals = mealsDataAccessObject.getMealsForUser(user.getName());
                userDataAccessObject.setCurrentUsername(username);
                final LoginOutputData loginOutputData = new LoginOutputData(
                        user.getName(),
                        user.getHeight(),
                        user.getWeight(),
                        user.getActivityLevel(),
                        user.getGoal(),
                        user.getProfilePicturePath(),
                        meals,
                        false
                );
                loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }
    @Override
    public void switchToSignupView() {
        loginPresenter.switchToSignupView();
    }
}
