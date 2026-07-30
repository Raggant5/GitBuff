package use_case.nutrition.food.add_food;

/**
 * The output boundary for the Add Food Entry Use Case.
 */
public interface AddFoodEntryOutputBoundary {
    /**
     * Prepares the success view for the Add Food Entry Case.
     * @param outputData the output data
     */
    void prepareSuccessView(AddFoodEntryOutputData outputData);

    /**
     * Prepares the failure view for the Add Food Entry Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
