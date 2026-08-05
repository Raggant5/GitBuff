package use_case.recommendation;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.ActivityLevel;
import entity.FitnessGoal;
import entity.User;
import entity.WorkoutPlan;

/**
 * Interactor that computes target macros and generates structured AI workout routines.
 */
public class RecommendationInteractor implements RecommendationInputBoundary {

    private static final double RESTING_KCAL_PER_KG = 22.0;
    private static final int DEFAULT_DURATION_MINUTES = 45;
    private static final int WEEK_DAYS = 7;

    private final RecommendationUserDataAccessInterface userDataAccessObject;
    private final RecommendationOutputBoundary recommendationPresenter;
    private final AiWorkoutDataAccessInterface aiWorkoutDataAccessObject;

    /**
     * Constructs a RecommendationInteractor instance.
     *
     * @param userDataAccessObject user data access interface
     * @param recommendationOutputBoundary presenter output boundary
     * @param aiWorkoutDataAccessObject AI workout generation data access interface
     */
    public RecommendationInteractor(final RecommendationUserDataAccessInterface userDataAccessObject,
                                    final RecommendationOutputBoundary recommendationOutputBoundary,
                                    final AiWorkoutDataAccessInterface aiWorkoutDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;
        this.recommendationPresenter = recommendationOutputBoundary;
        this.aiWorkoutDataAccessObject = aiWorkoutDataAccessObject;
    }

    @Override
    public void execute() {
        final String username = this.userDataAccessObject.getCurrentUsername();
        if (username == null) {
            this.recommendationPresenter.prepareFailView("No user is currently logged in.");
            return;
        }

        final User user = this.userDataAccessObject.get(username);
        if (user == null) {
            this.recommendationPresenter.prepareFailView("User details could not be loaded.");
            return;
        }

        // Apply fallback defaults if profile fields were left blank or uninitialized
        ensureProfileDefaults(user);

        // Calculate macro targets relative to user metrics
        final double restingCalories = RESTING_KCAL_PER_KG * (user.getWeight() > 0 ? user.getWeight() : 70.0f);
        final double activityMultiplier = user.getActivityLevel() != null
                ? user.getActivityLevel().getCalorieMultiplier()
                : ActivityLevel.MODERATELY_ACTIVE.getCalorieMultiplier();

        final double maintenanceCalories = restingCalories * activityMultiplier;
        final int goalAdjustment = user.getGoal() != null ? user.getGoal().getDailyCalorieAdjustment() : 0;
        final int dailyCalorieTarget = (int) Math.round(maintenanceCalories + goalAdjustment);

        final double proteinRatio = user.getGoal() != null ? user.getGoal().getProteinGramsPerKg() : 1.6;
        final int dailyProteinGrams = (int) Math.round(
                (user.getWeight() > 0 ? user.getWeight() : 70.0f) * proteinRatio
        );

        // Generate one week of workout plans
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

        final RecommendationOutputData outputData = new RecommendationOutputData(
                user.getBMI(),
                dailyCalorieTarget,
                dailyProteinGrams,
                focusSummary,
                activitySummary,
                plans
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
