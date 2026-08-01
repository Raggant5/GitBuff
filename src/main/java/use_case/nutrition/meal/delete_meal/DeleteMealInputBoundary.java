package use_case.nutrition.meal.delete_meal;

/**
 * Input Boundary for actions which are related to deleting a meal from the meal history.
 */
public interface DeleteMealInputBoundary {

    /**
     * Executes the delete meal use case.
     * @param deleteMealInputData the meal id for meal to be deleted
     */
    void execute(DeleteMealInputData deleteMealInputData);
}
