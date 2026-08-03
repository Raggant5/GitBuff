package use_case.nutrition.food.search_food;

public interface SearchFoodOutputBoundary {

    /**
     * Prepares the success view for searching for food.
     * @param outputData the foods provided to choose from
     */
    void prepareSuccessView(SearchFoodOutputData outputData);

    /**
     * Prepares the failure view for searching for food.
     * @param errorMessage explanation for failure
     */
    void prepareFailView(String errorMessage);
}
