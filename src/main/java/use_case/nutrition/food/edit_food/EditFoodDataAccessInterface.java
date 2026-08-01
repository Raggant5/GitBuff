package use_case.nutrition.food.edit_food;

import entity.FoodEntry;

/**
 * DAO for editing food entries.
 */
public interface EditFoodDataAccessInterface {

    /**
     * Updates a food entry.
     * @param foodEntry the updated food entry
     */
    void editFoodEntry(FoodEntry foodEntry);
}
