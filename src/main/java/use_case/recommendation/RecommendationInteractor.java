package use_case.recommendation;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.ActivityLevel;
import entity.FitnessGoal;
import entity.MealRecommendation;
import entity.User;
import entity.WorkoutPlan;

/**
 * Interactor implementing the business logic for recommendation generation.
 * Interactor that computes target macros and generates structured AI workout routines and meal suggestions.
 */
public class RecommendationInteractor implements RecommendationInputBoundary {

    private static final double RESTING_KCAL_PER_KG = 22.0;
    private static final int DEFAULT_DURATION_MINUTES = 45;
    private static final int WEEK_DAYS = 7;

    private final RecommendationUserDataAccessInterface userDataAccessObject;
    private final RecommendationOutputBoundary recommendationPresenter;
    private final AiWorkoutDataAccessInterface aiWorkoutDataAccessObject;
    private final FoodRecommendationDataAccessInterface foodRecommendationDataAccessObject;

    /**
     * Constructs a RecommendationInteractor instance.
     *
     * @param userDataAccessObject data access object for user profile lookup
     * @param recommendationOutputBoundary presenter output boundary
     * @param aiWorkoutDataAccessObject AI workout data access object
     * @param foodRecommendationDataAccessObject meal suggestion data access interface
     */
    public RecommendationInteractor(final RecommendationUserDataAccessInterface userDataAccessObject,
                                    final RecommendationOutputBoundary recommendationOutputBoundary,
                                    final AiWorkoutDataAccessInterface aiWorkoutDataAccessObject,
                                    final FoodRecommendationDataAccessInterface foodRecommendationDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;
        this.recommendationPresenter = recommendationOutputBoundary;
        this.aiWorkoutDataAccessObject = aiWorkoutDataAccessObject;
        this.foodRecommendationDataAccessObject = foodRecommendationDataAccessObject;
    }

    @Override
    public void execute() {
        final String username = this.userDataAccessObject.getCurrentUsername();
        if (username == null) {
            this.recommendationPresenter.prepareFailView("No user is currently logged in.");
        }
        else {
            final User user = this.userDataAccessObject.get(username);
            if (user == null || user.getWeight() <= 0.0f || user.getHeight() <= 0.0f) {
                final RecommendationOutputData defaultOutput = new RecommendationOutputData(
                        0.0, 0, 0, "General Fitness", "Please update your profile details.",
                        new ArrayList<>(), new ArrayList<>()
                );
                this.recommendationPresenter.prepareSuccessView(defaultOutput);
            }
            else {
                presentRecommendationFor(user);
            }
        }
    }

    private void presentRecommendationFor(final User user) {
        final double restingCalories = RESTING_KCAL_PER_KG * user.getWeight();
        final double maintenanceCalories = restingCalories * user.getActivityLevel().getCalorieMultiplier();
        final int dailyCalorieTarget =
                (int) Math.round(maintenanceCalories + user.getGoal().getDailyCalorieAdjustment());
        final int dailyProteinGrams = (int) Math.round(user.getWeight() * user.getGoal().getProteinGramsPerKg());

        List<WorkoutPlan> plans = new ArrayList<>();
        if (this.aiWorkoutDataAccessObject != null) {
            plans = this.aiWorkoutDataAccessObject.generateWorkoutPlans(user, WEEK_DAYS);
        }

        final String focusSummary = user.getGoal() != null
                ? user.getGoal().getWorkoutFocus()
                : FitnessGoal.MAINTAIN_GENERAL_FITNESS.getWorkoutFocus();

        final String activitySummary = user.getActivityLevel() != null
                ? user.getActivityLevel().getDescription()
                : ActivityLevel.MODERATELY_ACTIVE.getDescription();
        List<MealRecommendation> meals = new ArrayList<>();
        if (this.foodRecommendationDataAccessObject != null) {
            meals = this.foodRecommendationDataAccessObject.generateMealRecommendations(user, dailyCalorieTarget);
        }

        final RecommendationOutputData outputData = new RecommendationOutputData(
                user.getBmi(),
                dailyCalorieTarget,
                dailyProteinGrams,
                focusSummary,
                activitySummary,
                plans
                meals
        );

        this.recommendationPresenter.prepareSuccessView(outputData);
    }

    private void ensureProfileDefaults(final User user) {
        if (user.getGoal() == null) {
            user.setGoal(FitnessGoal.MAINTAIN_GENERAL_FITNESS);
        }
        if (user.getActivityLevel() == null) {
            user.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        }
        if (user.getPreferredWorkoutDays() == null || user.getPreferredWorkoutDays().isEmpty()) {
            final Set<DayOfWeek> defaultDays = new HashSet<>();
            defaultDays.add(DayOfWeek.MONDAY);
            defaultDays.add(DayOfWeek.WEDNESDAY);
            defaultDays.add(DayOfWeek.FRIDAY);
            user.setPreferredWorkoutDays(defaultDays);
        }
        if (user.getPreferredWorkoutDurationMinutes() <= 0) {
            user.setPreferredWorkoutDurationMinutes(DEFAULT_DURATION_MINUTES);
        }
    }
}
