package use_case.recommendation;

import java.util.List;

import entity.MealRecommendation;
import entity.User;

/**
 * Interface for generating meal suggestions matching a user's daily calorie target, via external API.
 */
public interface FoodRecommendationDataAccessInterface {

    /**
     * Generates a list of suggested meals for the given user and calorie target.
     *
     * @param user the user requesting recommendations
     * @param targetCalories the user's daily calorie target
     * @return a list of suggested meals
     */
    List<MealRecommendation> generateMealRecommendations(final User user, final int targetCalories);
}
