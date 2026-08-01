package use_case.nutrition.food.delete_food;

/**
 * Input Boundary for actions which are related to deleting food from the food history.
 */
public interface DeleteFoodInputBoundary {

    /**
     * Executes the delete food use case.
     * @param deleteFoodInputData the food id for food to be deleted
     */
    void execute(DeleteFoodInputData deleteFoodInputData);
}
