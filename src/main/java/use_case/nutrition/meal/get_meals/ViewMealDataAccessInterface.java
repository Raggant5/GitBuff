package use_case.nutrition.meal.get_meals;

import java.util.List;

import entity.FoodEntry;
import entity.Meal;

/**
 * DAO for viewing nutrition data.
 */
public interface ViewMealDataAccessInterface {

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

    /**
     * Gets a single meal, including its food entries, by id.
     * @param mealId the meal id
     * @return the meal
     */
    Meal getMealById(int mealId);
}
