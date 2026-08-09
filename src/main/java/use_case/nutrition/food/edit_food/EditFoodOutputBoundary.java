package use_case.nutrition.food.edit_food;

import use_case.nutrition.food.FoodValidationErrors;

public interface EditFoodOutputBoundary {

    /**
     * Prepares the success view after editing a food entry.
     * @param outputData updated food entry data
     */
    void prepareSuccessView(EditFoodOutputData outputData);

    /**
     * Prepares the failure view.
     * @param errors per-field reason the edit failed
     */
    void prepareFailView(FoodValidationErrors errors);

}
