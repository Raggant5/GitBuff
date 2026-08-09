package interface_adapter.login;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import entity.ExercisePerformed;
import entity.FoodEntry;
import entity.LoggedWorkout;
import entity.Meal;
import interface_adapter.ViewManagerModel;
import interface_adapter.calendar.CalendarController;
import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import interface_adapter.log_workout.exercise.StrengthDetailsDisplayData;
import interface_adapter.log_workout.workout.LoggedWorkoutDisplayData;
import interface_adapter.log_workout.workout.ViewWorkoutsViewModel;
import interface_adapter.nutrition.food.FoodEntryDisplayData;
import interface_adapter.nutrition.food.FoodNutritionDisplayData;
import interface_adapter.nutrition.meal.MealDisplayData;
import interface_adapter.nutrition.meal.ViewMealsViewModel;
import interface_adapter.profile.ProfileEnumMapper;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import interface_adapter.recommendation.RecommendationController;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.dashboard.DashboardInputBoundary;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 *
 * <p>Also seeds {@link ProfileState}, the meals view, and the workout-history view from the
 * user data returned on login. Entity-layer profile enums carried by {@link LoginOutputData}
 * are translated to the interface_adapter-layer {@code *Option} enums via
 * {@link ProfileEnumMapper} before reaching {@link ProfileState}, consistent with how
 * {@code interface_adapter.profile.ProfilePresenter} does the same translation after a profile
 * edit.
 *
 * <p><strong>Known design smell:</strong> {@link #prepareSuccessView(LoginOutputData)} still
 * reaches directly into six collaborators (login, profile, meals, workout-history, workouts,
 * calendar, recommendation, and dashboard) - a Single Responsibility Principle violation, since
 * this class has a reason to change whenever any of those features' post-login behavior
 * changes. It has been split into one private method per collaborator here so each step is at
 * least independently readable and testable, but the deeper fix - having each feature observe a
 * "user logged in" event instead of being reached into by this presenter - would require
 * introducing a login-event publisher/subscriber mechanism and rewiring
 * {@code app.AppBuilder} for every feature listed above. That's a larger, higher-risk change
 * than this pass attempted; it's recorded here as the recommended next step rather than applied
 * blind.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;
    private final SignupViewModel signupViewModel;
    private final ProfileViewModel profileViewModel;
    private final ViewMealsViewModel mealsViewModel;
    private final WorkoutsViewModel workoutsViewModel;
    private final ViewWorkoutsViewModel viewWorkoutsViewModel;
    private final RecommendationController recommendationController;
    private final DashboardInputBoundary dashboardInteractor;
    private final CalendarController calendarController;

    /**
     * Constructs a LoginPresenter instance.
     *
     * @param viewManagerModel manager model for top-level view navigation
     * @param loginViewModel view model for login state
     * @param signupViewModel view model for signup state
     * @param profileViewModel view model for user profile state
     * @param mealsViewModel view model for user meals state
     * @param workoutsViewModel view model for workouts state
     * @param recommendationController controller to trigger recommendations
     *        after login
     * @param dashboardInteractor interactor for loading dashboard data
     * @param viewWorkoutsViewModel view model for the user's workout history
     * @param calendarController controller for loading and synchronizing
     *        calendar events
     */
    public LoginPresenter(
            final ViewManagerModel viewManagerModel,
            final LoginViewModel loginViewModel,
            final SignupViewModel signupViewModel,
            final ProfileViewModel profileViewModel,
            final ViewMealsViewModel mealsViewModel,
            final WorkoutsViewModel workoutsViewModel,
            final RecommendationController recommendationController,
            final DashboardInputBoundary dashboardInteractor,
            final ViewWorkoutsViewModel viewWorkoutsViewModel,
            final CalendarController calendarController) {

        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.signupViewModel = signupViewModel;
        this.profileViewModel = profileViewModel;
        this.mealsViewModel = mealsViewModel;
        this.workoutsViewModel = workoutsViewModel;
        this.recommendationController = recommendationController;
        this.dashboardInteractor = dashboardInteractor;
        this.calendarController = calendarController;
        this.viewWorkoutsViewModel = viewWorkoutsViewModel;
    }

    @Override
    public void prepareSuccessView(final LoginOutputData response) {
        updateLoginState(response);
        updateProfileState(response);
        final List<MealDisplayData> meals = reloadMeals(response);
        reloadWorkoutHistory(response);
        markWorkoutScheduleLoading();
        synchronizeCalendar(meals);
        triggerRecommendationRefresh();
        refreshDashboard(response);
        navigateToAppShell();
    }

    private void updateLoginState(final LoginOutputData response) {
        final LoginState loginState = this.loginViewModel.getState();
        loginState.setUsername(response.getUsername());
        loginState.setLoginError(null);
        this.loginViewModel.firePropertyChanged();
    }

    private void updateProfileState(final LoginOutputData response) {
        final ProfileState profileState = this.profileViewModel.getState();

        profileState.setUsername(response.getUsername());
        profileState.setHeightText(String.valueOf(response.getHeight()));
        profileState.setWeightText(String.valueOf(response.getWeight()));
        profileState.setActivityLevel(ProfileEnumMapper.toOption(response.getActivityLevel()));
        profileState.setGoal(ProfileEnumMapper.toOption(response.getGoal()));
        profileState.setProfilePicturePath(response.getProfilePicturePath());
        profileState.setDateOfBirth(response.getDateOfBirth());
        profileState.setGender(ProfileEnumMapper.toOption(response.getGender()));
        profileState.setBio(response.getBio());
        profileState.setPreferredUnitSystem(ProfileEnumMapper.toOption(response.getPreferredUnitSystem()));
        profileState.setEquipment(ProfileEnumMapper.toEquipmentOptions(response.getEquipment()));
        profileState.setDietaryRestrictions(
                ProfileEnumMapper.toDietaryOptions(response.getDietaryRestrictions()));
        profileState.setPreferredWorkoutDays(response.getPreferredWorkoutDays());
        profileState.setPreferredWorkoutDurationMinutes(response.getPreferredWorkoutDurationMinutes());
        profileState.setPrivacySettings(ProfileEnumMapper.toPrivacyOptions(response.getPrivacySettings()));
        profileState.setProfileError(null);
        profileState.setSaveConfirmation(null);

        this.profileViewModel.firePropertyChanged();
    }

    /**
     * Reloads the user's saved meals into the meals view, returning the display data so the
     * caller can also hand it to the calendar controller without recomputing it.
     *
     * @param response the login output data
     * @return the user's meals, converted to display data
     */
    private List<MealDisplayData> reloadMeals(final LoginOutputData response) {
        final List<MealDisplayData> meals = new ArrayList<>();
        for (final Meal meal : response.getMeals()) {
            final List<FoodEntryDisplayData> foodEntries = new ArrayList<>();
            for (final FoodEntry food : meal.getFoodEntries()) {
                final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData(
                        food.getNutrition().getCalories(), food.getNutrition().getProtein(),
                        food.getNutrition().getCarbs(), food.getNutrition().getFat());
                foodEntries.add(new FoodEntryDisplayData(food.getId(), food.getFoodName(), nutrition,
                        food.getQuantity(), food.getUnit(), food.getGrams()));
            }
            meals.add(new MealDisplayData(meal.getId(), meal.getDate(), meal.getName(), foodEntries));
        }

        this.mealsViewModel.getState().setMeals(meals);
        this.mealsViewModel.firePropertyChanged();
        return meals;
    }

    private void reloadWorkoutHistory(final LoginOutputData response) {
        final List<LoggedWorkoutDisplayData> workouts = new ArrayList<>();
        for (final LoggedWorkout workout : response.getWorkouts()) {
            final List<ExercisePerformedDisplayData> exercises = new ArrayList<>();
            for (final ExercisePerformed exercise : workout.getExercises()) {
                final StrengthDetailsDisplayData strengthDetailsDisplayData = new StrengthDetailsDisplayData(
                        exercise.getSets(), exercise.getReps(), exercise.getWeight());
                exercises.add(new ExercisePerformedDisplayData(exercise.getId(), exercise.getExerciseName(),
                        strengthDetailsDisplayData, exercise.getDurationMins(), exercise.getDistanceKm(),
                        exercise.getIsCardio()));
            }
            workouts.add(new LoggedWorkoutDisplayData(workout.getId(), workout.getDate(), exercises));
        }

        this.viewWorkoutsViewModel.getState().setWorkouts(workouts);
        this.viewWorkoutsViewModel.firePropertyChanged();
    }

    private void markWorkoutScheduleLoading() {
        if (this.workoutsViewModel != null) {
            final WorkoutsState workoutsState = this.workoutsViewModel.getState();
            workoutsState.setLoading(true);
            this.workoutsViewModel.firePropertyChanged();
        }
    }

    private void synchronizeCalendar(final List<MealDisplayData> meals) {
        if (this.calendarController != null) {
            this.calendarController.loadCalendarEvents();
            this.calendarController.synchronizeMeals(meals);
        }
    }

    private void triggerRecommendationRefresh() {
        if (this.recommendationController != null) {
            final SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    recommendationController.execute();
                    return null;
                }
            };
            worker.execute();
        }
    }

    private void refreshDashboard(final LoginOutputData response) {
        if (this.dashboardInteractor != null) {
            this.dashboardInteractor.execute(response.getUsername());
        }
    }

    private void navigateToAppShell() {
        this.viewManagerModel.setState("app shell");
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(final String error) {

        final LoginState loginState =
                this.loginViewModel.getState();

        loginState.setLoginError(error);

        this.loginViewModel.firePropertyChanged();
    }

    @Override
    public void switchToSignupView() {

        this.viewManagerModel.setState(
                this.signupViewModel.getViewName()
        );

        this.viewManagerModel.firePropertyChanged();
    }
}