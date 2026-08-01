package use_case.recommendation;

import java.util.List;

import entity.WorkoutPlan;

/**
 * Output Data for Recommendation Use Case.
 */
public class RecommendationOutputData {

    private final double bmi;
    private final int dailyCalorieTarget;
    private final int dailyProteinGrams;
    private final String workoutFocus;
    private final String activityLevelDescription;
    private final List<WorkoutPlan> workoutPlans;

    /**
     * Constructs a RecommendationOutputData instance.
     *
     * @param bmi calculated Body Mass Index
     * @param dailyCalorieTarget calculated daily calorie target
     * @param dailyProteinGrams calculated daily protein target in grams
     * @param workoutFocus text strategy summary of workout goal
     * @param activityLevelDescription activity level explanation
     * @param workoutPlans list of structured 2-week AI workout plans
     */
    public RecommendationOutputData(final double bmi, final int dailyCalorieTarget, final int dailyProteinGrams,
                                    final String workoutFocus, final String activityLevelDescription,
                                    final List<WorkoutPlan> workoutPlans) {
        this.bmi = bmi;
        this.dailyCalorieTarget = dailyCalorieTarget;
        this.dailyProteinGrams = dailyProteinGrams;
        this.workoutFocus = workoutFocus;
        this.activityLevelDescription = activityLevelDescription;
        this.workoutPlans = workoutPlans;
    }

    public double getBmi() {
        return this.bmi;
    }

    public int getDailyCalorieTarget() {
        return this.dailyCalorieTarget;
    }

    public int getDailyProteinGrams() {
        return this.dailyProteinGrams;
    }

    public String getWorkoutFocus() {
        return this.workoutFocus;
    }

    public String getActivityLevelDescription() {
        return this.activityLevelDescription;
    }

    public List<WorkoutPlan> getWorkoutPlans() {
        return this.workoutPlans;
    }
}
