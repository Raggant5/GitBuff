package use_case.nutrition.meal.get_meals;

/**
 * Output boundary for the Get Meals Use Case.
 */
public interface GetMealsOutputBoundary {

    /**
     * Prepares the success view for the Get Meals Use Case.
     *
     * @param outputData the user's saved meals
     */
    void prepareSuccessView(GetMealsOutputData outputData);

    /**
     * Prepares the failure view for the Get Meals Use Case.
     *
     * @param errorMessage explanation of failure cause
     */
    void prepareFailView(String errorMessage);
}
