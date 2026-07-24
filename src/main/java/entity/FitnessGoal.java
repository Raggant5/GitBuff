package entity;

/**
 * A user's overall fitness objective. Used to adjust calorie and protein
 * recommendations relative to the user's maintenance calories.
 */
public enum FitnessGoal {

    LOSE_WEIGHT("Lose Weight", -500, 2.2,
            "Push/Pull/Legs with added cardio to support a calorie deficit"),
    MAINTAIN("Maintain", 0, 1.6,
            "Balanced full-body training to maintain current fitness"),
    GAIN_MUSCLE("Gain Muscle", 300, 2.0,
            "Upper/Lower split focused on progressive overload");

    private final String displayName;
    private final int dailyCalorieAdjustment;
    private final double proteinGramsPerKg;
    private final String workoutFocus;

    FitnessGoal(String displayName, int dailyCalorieAdjustment, double proteinGramsPerKg, String workoutFocus) {
        this.displayName = displayName;
        this.dailyCalorieAdjustment = dailyCalorieAdjustment;
        this.proteinGramsPerKg = proteinGramsPerKg;
        this.workoutFocus = workoutFocus;
    }

    /**
     * Returns the number of calories to add to (or subtract from) a user's
     * maintenance calories to work towards this goal.
     * @return the daily calorie adjustment, in kcal.
     */
    public int getDailyCalorieAdjustment() {
        return dailyCalorieAdjustment;
    }

    /**
     * Returns the recommended protein intake per kilogram of body weight for this goal.
     * @return grams of protein per kilogram of body weight.
     */
    public double getProteinGramsPerKg() {
        return proteinGramsPerKg;
    }

    public String getWorkoutFocus() {
        return workoutFocus;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
