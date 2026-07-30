package use_case.nutrition.meal.add_meal;

/**
 * The output boundary for the Add Meal Use Case.
 */
public interface AddMealOutputBoundary {
    /**
     * Prepares the success view for the Add Meal Case.
     * @param outputData the output data
     */
    void prepareSuccessView(AddMealOutputData outputData);

    /**
     * Prepares the failure view for the Add Meal Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
