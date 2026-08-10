package use_case.login;

import java.util.List;

import entity.LoggedWorkout;
import entity.Meal;
import entity.User;
import use_case.DataAccessException;
import use_case.log_workout.logged_workout.get_workouts.ViewWorkoutDataAccessInterface;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;

/**
 * Interactor implementing business logic for the Login Use Case.
 */
public class LoginInteractor implements LoginInputBoundary {

    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;
    private final ViewMealDataAccessInterface mealsDataAccessObject;
    private final ViewWorkoutDataAccessInterface workoutsDataAccessObject;

    /**
     * Constructs a LoginInteractor instance.
     *
     * @param userDataAccessInterface user data access persistence object
     * @param loginOutputBoundary output boundary presenter
     * @param mealsDataAccessObject the meal data access persistence object
     * @param workoutsDataAccessObject the workout data access persistence object
     */
    public LoginInteractor(final LoginUserDataAccessInterface userDataAccessInterface,
                           final LoginOutputBoundary loginOutputBoundary,
                           final ViewMealDataAccessInterface mealsDataAccessObject,
                           final ViewWorkoutDataAccessInterface workoutsDataAccessObject) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
        this.mealsDataAccessObject = mealsDataAccessObject;
        this.workoutsDataAccessObject = workoutsDataAccessObject;
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
            final List<LoggedWorkout> workouts = workoutsDataAccessObject.getWorkoutsForUser(user.getName());
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
                    workouts,
                    false
            );

            loginPresenter.prepareSuccessView(loginOutputData);
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



