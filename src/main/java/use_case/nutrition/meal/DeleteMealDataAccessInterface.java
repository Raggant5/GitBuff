package use_case.nutrition.meal;

/**
 * DAO for deleting meals.
 */
public interface DeleteMealDataAccessInterface {

    /**
     * Deletes a meal.
     * @param mealId the meal id
     */
    void deleteMeal(int mealId);
}
