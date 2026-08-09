package use_case.nutrition.food.create_food;

import use_case.nutrition.food.FoodValidationErrors;

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
     * @param errors the per-field explanation of the failure
     */
    void prepareFailView(FoodValidationErrors errors);
}
