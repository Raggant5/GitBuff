package use_case.recommendation;

import entity.User;

/**
 * Default {@link CalorieCalculationStrategy}: a resting-energy-per-kilogram estimate, scaled by
 * the user's activity level and then adjusted by their fitness goal.
 *
 * <p>This reproduces the formula GitBuff has always used for recommendations. It is now
 * isolated behind {@link CalorieCalculationStrategy} so it can be swapped out, or additional
 * strategies added, without changing {@link RecommendationInteractor}.
 */
public class StandardCalorieCalculationStrategy implements CalorieCalculationStrategy {

    private static final double RESTING_KCAL_PER_KG = 22.0;

    @Override
    public int calculateDailyCalorieTarget(final User user) {
        final double restingCalories = RESTING_KCAL_PER_KG * user.getWeight();
        final double maintenanceCalories = restingCalories * user.getActivityLevel().getCalorieMultiplier();
        return (int) Math.round(maintenanceCalories + user.getGoal().getDailyCalorieAdjustment());
    }

    @Override
    public int calculateDailyProteinGrams(final User user) {
        return (int) Math.round(user.getWeight() * user.getGoal().getProteinGramsPerKg());
    }
}



