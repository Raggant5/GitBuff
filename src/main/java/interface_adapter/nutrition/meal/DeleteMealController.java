package interface_adapter.nutrition.meal;

import use_case.nutrition.meal.delete_meal.DeleteMealInputBoundary;
import use_case.nutrition.meal.delete_meal.DeleteMealInputData;

/**
 * Controller for deleting a meal.
 */
public class DeleteMealController {
    private final DeleteMealInputBoundary deleteMealInputInteractor;

    public DeleteMealController(DeleteMealInputBoundary deleteMealInputInteractor) {
        this.deleteMealInputInteractor = deleteMealInputInteractor;
    }

    /**
     * Executes the Delete Meal Use Case.
     * @param mealId id of the meal to delete
     */
    public void execute(int mealId) {
        deleteMealInputInteractor.execute(new DeleteMealInputData(mealId));
    }

}
