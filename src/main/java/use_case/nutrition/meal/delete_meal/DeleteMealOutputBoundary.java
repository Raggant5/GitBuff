package use_case.nutrition.meal.delete_meal;

/**
 * The output boundary for the Delete Meal Use Case.
 */
public interface DeleteMealOutputBoundary {
    /**
     * Prepares the success view for the Delete Meal Case.
     * @param deleteMealOutputData contains the meal id to be deleted
     */
    void prepareSuccessView(DeleteMealOutputData deleteMealOutputData);

    /**
     * Prepares the failure view for the Delete Meal Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
