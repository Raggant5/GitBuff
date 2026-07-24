package entity;

/**
 * How physically active a user is day-to-day, outside of planned workouts.
 * Used to scale a user's resting energy expenditure into a daily calorie target.
 */
public enum ActivityLevel {

    SEDENTARY(1.2, "Sedentary (little or no exercise)"),
    LIGHTLY_ACTIVE(1.375, "Lightly active (light exercise 1-3 days/week)"),
    MODERATELY_ACTIVE(1.55, "Moderately active (moderate exercise 3-5 days/week)"),
    VERY_ACTIVE(1.725, "Very active (hard exercise 6-7 days/week)"),
    EXTRA_ACTIVE(1.9, "Extra active (very hard exercise & physical job)");

    private final double calorieMultiplier;
    private final String description;

    ActivityLevel(double calorieMultiplier, String description) {
        this.calorieMultiplier = calorieMultiplier;
        this.description = description;
    }

    /**
     * Returns the multiplier applied to a user's resting energy expenditure
     * to estimate their total daily energy expenditure.
     * @return the calorie multiplier for this activity level.
     */
    public double getCalorieMultiplier() {
        return calorieMultiplier;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
