package entity;

/**
 * How physically active a user is day-to-day, outside of planned workouts.
 */
public enum ActivityLevel {

    NOT_ACTIVE(1.2, "Not active (little or no exercise)"),
    LIGHTLY_ACTIVE(1.375, "Lightly active (light exercise 1-3 days/week)"),
    MODERATELY_ACTIVE(1.55, "Moderately active (moderate exercise 3-5 days/week)"),
    VERY_ACTIVE(1.725, "Very active (hard exercise 6-7 days/week)"),
    EXTRA_ACTIVE(1.9, "Extra active (very hard exercise & physical job)");

    private final double calorieMultiplier;
    private final String description;

    ActivityLevel(final double calorieMultiplier, final String description) {
        this.calorieMultiplier = calorieMultiplier;
        this.description = description;
    }

    /**
     * Returns the multiplier applied to a user's resting energy expenditure.
     *
     * @return the calorie multiplier.
     */
    public double getCalorieMultiplier() {
        return this.calorieMultiplier;
    }

    /**
     * Returns the description of activity level.
     *
     * @return description string.
     */
    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
