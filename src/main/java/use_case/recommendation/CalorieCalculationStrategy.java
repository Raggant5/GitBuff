package use_case.recommendation;

import entity.User;

/**
 * Strategy for estimating a user's daily calorie and protein targets from their profile.
 *
 * <p>A concrete strategy decides how resting energy expenditure is estimated and how it is
 * combined with the user's activity level and fitness goal. Injecting a strategy into
 * {@link RecommendationInteractor}, rather than hard-coding a formula there, keeps the
 * interactor open for new calculation methods (for example Harris-Benedict or Katch-McArdle)
 * without modifying its logic - the Open/Closed Principle applied through the Strategy design
 * pattern.
 */
public interface CalorieCalculationStrategy {

    /**
     * Estimates the user's daily calorie target.
     *
     * @param user the user whose profile drives the calculation
     * @return the estimated daily calorie target, in kilocalories
     */
    int calculateDailyCalorieTarget(User user);

    /**
     * Estimates the user's daily protein target.
     *
     * @param user the user whose profile drives the calculation
     * @return the estimated daily protein target, in grams
     */
    int calculateDailyProteinGrams(User user);
}

