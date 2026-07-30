package use_case.nutrition.meal.edit_meal;

public interface EditMealOutputBoundary {

    /**
     * Prepares the success response.
     * @param outputData data returned after editing the meal successfully
     */
    void prepareSuccessView(EditMealOutputData outputData);

    /**
     * Prepares the failure response.
     * @param errorMessage reason the edit failed
     */
    void prepareFailView(String errorMessage);

}
