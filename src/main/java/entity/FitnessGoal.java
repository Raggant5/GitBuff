package entity;

/**
 * A user's overall fitness objective.
 */
public enum FitnessGoal {

    LOSE_WEIGHT("Lose Weight", -500, 2.2,
            "Push/Pull/Legs with added cardio to support a calorie deficit"),
    MAINTAIN_GENERAL_FITNESS("Maintain Weight & General Fitness", 0, 1.6,
            "Balanced full-body training to maintain overall fitness and health"),
    MUSCLE_AND_STRENGTH_GAIN("Muscle & Strength Gain", 300, 2.0,
            "Upper/Lower split focused on progressive overload and strength milestones"),
    INCREASE_ENDURANCE("Increase Endurance", -100, 1.8,
            "Cardio, stamina, and aerobic conditioning focused routines"),
    FLEXIBILITY_AND_MOBILITY("Flexibility & Mobility", 0, 1.4,
            "Yoga, stretching, core stability, and joint recovery routines");

    private final String displayName;
    private final int dailyCalorieAdjustment;
    private final double proteinGramsPerKg;
    private final String workoutFocus;

    FitnessGoal(final String displayName, final int dailyCalorieAdjustment,
                final double proteinGramsPerKg, final String workoutFocus) {
        this.displayName = displayName;
        this.dailyCalorieAdjustment = dailyCalorieAdjustment;
        this.proteinGramsPerKg = proteinGramsPerKg;
        this.workoutFocus = workoutFocus;
    }

    /**
     * Gets the daily calorie adjustment value relative to TDEE.
     *
     * @return daily calorie adjustment.
     */
    public int getDailyCalorieAdjustment() {
        return this.dailyCalorieAdjustment;
    }

    /**
     * Gets the protein recommendation in grams per kilogram of body weight.
     *
     * @return protein ratio per kg.
     */
    public double getProteinGramsPerKg() {
        return this.proteinGramsPerKg;
    }

    /**
     * Gets the description of the workout focus strategy.
     *
     * @return workout focus description.
     */
    public String getWorkoutFocus() {
        return this.workoutFocus;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
