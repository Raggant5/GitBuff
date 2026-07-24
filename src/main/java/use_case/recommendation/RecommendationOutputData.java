package use_case.recommendation;

/**
 * Output Data for the Recommendation Use Case.
 */
public class RecommendationOutputData {

    private final double bmi;
    private final int dailyCalorieTarget;
    private final int dailyProteinGrams;
    private final String workoutFocus;
    private final String activityLevelDescription;

    public RecommendationOutputData(double bmi, int dailyCalorieTarget, int dailyProteinGrams,
                                     String workoutFocus, String activityLevelDescription) {
        this.bmi = bmi;
        this.dailyCalorieTarget = dailyCalorieTarget;
        this.dailyProteinGrams = dailyProteinGrams;
        this.workoutFocus = workoutFocus;
        this.activityLevelDescription = activityLevelDescription;
    }

    public double getBmi() {
        return bmi;
    }

    public int getDailyCalorieTarget() {
        return dailyCalorieTarget;
    }

    public int getDailyProteinGrams() {
        return dailyProteinGrams;
    }

    public String getWorkoutFocus() {
        return workoutFocus;
    }

    public String getActivityLevelDescription() {
        return activityLevelDescription;
    }
}
