package use_case.nutrition.meal;

import java.util.List;

import entity.FoodEntry;
import entity.Meal;

/**
 * DAO for viewing nutrition data.
 */
public interface ViewNutritionDataAccessInterface {

    /**
     * Gets all meals for a user.
     * @param userId the user id
     * @return the user's meals
     */
    List<Meal> getMealsForUser(String userId);

    /**
     * Gets the food entries for a meal.
     * @param mealId the meal id
     * @return the food entries
     */
    List<FoodEntry> getFoodEntriesForMeal(int mealId);
}
