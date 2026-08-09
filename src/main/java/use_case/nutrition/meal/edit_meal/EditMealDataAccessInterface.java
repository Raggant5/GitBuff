package use_case.nutrition.meal.edit_meal;

import java.util.List;

import entity.Meal;

public interface EditMealDataAccessInterface {

    /**
     * Updates a meal, persisting its food entries (inserting new ones, updating existing ones)
     * and deleting any removed food entries, all as a single atomic operation.
     * @param meal the updated meal
     * @param foodEntryIdsToDelete ids of food entries to remove from the meal
     * @return the persisted meal, with generated ids populated on any newly-inserted food entries
     */
    Meal editMeal(Meal meal, List<Integer> foodEntryIdsToDelete);
}
