package use_case.nutrition.food.edit_food;

public interface EditFoodOutputBoundary {

    /**
     * Prepares the success view after editing a food entry.
     * @param outputData updated food entry data
     */
    void prepareSuccessView(EditFoodOutputData outputData);

    /**
     * Prepares the failure view.
     * @param errorMessage reason the edit failed
     */
    void prepareFailView(String errorMessage);

}
