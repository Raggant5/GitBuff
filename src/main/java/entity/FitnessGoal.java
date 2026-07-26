package entity;

/**
 * A user's overall fitness objective. Used to adjust calorie and protein
 * recommendations relative to the user's maintenance calories.
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

    FitnessGoal(String displayName, int dailyCalorieAdjustment, double proteinGramsPerKg, String workoutFocus) {
        this.displayName = displayName;
        this.dailyCalorieAdjustment = dailyCalorieAdjustment;
        this.proteinGramsPerKg = proteinGramsPerKg;
        this.workoutFocus = workoutFocus;
    }

    public int getDailyCalorieAdjustment() {
        return dailyCalorieAdjustment;
    }

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
