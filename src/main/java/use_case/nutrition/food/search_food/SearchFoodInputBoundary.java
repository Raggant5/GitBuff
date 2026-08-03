package use_case.nutrition.food.search_food;

public interface SearchFoodInputBoundary {

    /**
     * Executes the search for food use case.
     * @param searchFoodInputData contains the name to search with
     */
    void execute(SearchFoodInputData searchFoodInputData);
}
