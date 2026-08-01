package use_case.nutrition.meal.add_meal;

import entity.FoodEntry;
import entity.Meal;

/**
 * DAO for adding a meal.
 */
public interface AddMealDataAccessInterface {

    /**
     * Saves the meal.
     *
     * @param meal the meal to save
     * @return the generated meal id
     */
    int saveMeal(Meal meal);

    /**
     * Saves a food entry.
     *
     * @param foodEntry the food entry to save
     * @return the generated food entry id
     */
    int saveFoodEntry(FoodEntry foodEntry);
}
