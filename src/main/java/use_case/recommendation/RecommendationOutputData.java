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
    private final String aiWorkoutPlan;

    /**
     * Constructs a RecommendationOutputData instance.
     *
     * @param bmi calculated BMI
     * @param dailyCalorieTarget target daily calorie intake
     * @param dailyProteinGrams target daily protein intake
     * @param workoutFocus default focus text
     * @param activityLevelDescription activity level description
     * @param aiWorkoutPlan AI generated workout plan from Gemini
     */
    public RecommendationOutputData(final double bmi, final int dailyCalorieTarget, final int dailyProteinGrams,
                                    final String workoutFocus, final String activityLevelDescription,
                                    final String aiWorkoutPlan) {
        this.bmi = bmi;
        this.dailyCalorieTarget = dailyCalorieTarget;
        this.dailyProteinGrams = dailyProteinGrams;
        this.workoutFocus = workoutFocus;
        this.activityLevelDescription = activityLevelDescription;
        this.aiWorkoutPlan = aiWorkoutPlan;
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

    public String getAiWorkoutPlan() {
        return this.aiWorkoutPlan;
    }
}
