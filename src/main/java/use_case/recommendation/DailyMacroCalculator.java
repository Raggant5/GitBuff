package use_case.recommendation;

import entity.User;

/**
 * Shared calorie/protein target math used by every use case that needs a user's daily targets,
 * so the formula lives in exactly one place instead of being duplicated across interactors.
 */
public final class DailyMacroCalculator {

    private static final double RESTING_KCAL_PER_KG = 22.0;

    private DailyMacroCalculator() {
        // Utility class.
    }

    /**
     * Calculates the user's daily calorie target from resting energy expenditure, activity
     * level, and fitness goal.
     *
     * @param user the user to calculate a target for
     * @return the daily calorie target, rounded to the nearest whole calorie
     */
    public static int calculateDailyCalorieTarget(final User user) {
        final double restingCalories = RESTING_KCAL_PER_KG * user.getWeight();
        final double maintenanceCalories = restingCalories * user.getActivityLevel().getCalorieMultiplier();
        return (int) Math.round(maintenanceCalories + user.getGoal().getDailyCalorieAdjustment());
    }

    /**
     * Calculates the user's daily protein target from their weight and fitness goal.
     *
     * @param user the user to calculate a target for
     * @return the daily protein target in grams, rounded to the nearest whole gram
     */
    public static int calculateDailyProteinGrams(final User user) {
        return (int) Math.round(user.getWeight() * user.getGoal().getProteinGramsPerKg());
    }
}
