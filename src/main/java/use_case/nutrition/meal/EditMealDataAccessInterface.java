package use_case.nutrition.meal;

import entity.Meal;

/**
 * DAO for editing meals.
 */
public interface EditMealDataAccessInterface {

    /**
     * Updates a meal.
     * @param meal the updated meal
     */
    void updateMeal(Meal meal);
}
