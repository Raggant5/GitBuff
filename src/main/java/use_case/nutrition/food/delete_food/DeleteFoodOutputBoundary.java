package use_case.nutrition.food.delete_food;

/**
 * The output boundary for the Delete Food Use Case.
 */
public interface DeleteFoodOutputBoundary {
    /**
     * Prepares the success view for the Delete Food Case.
     * @param deleteFoodOutputData contains the food id to be deleted
     */
    void prepareSuccessView(DeleteFoodOutputData deleteFoodOutputData);
}
