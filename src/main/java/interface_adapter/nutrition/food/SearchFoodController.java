package interface_adapter.nutrition.food;

import use_case.nutrition.food.search_food.SearchFoodInputBoundary;
import use_case.nutrition.food.search_food.SearchFoodInputData;

public class SearchFoodController {

    private final SearchFoodInputBoundary searchFoodInteractor;

    public SearchFoodController(SearchFoodInputBoundary searchFoodInteractor) {
        this.searchFoodInteractor = searchFoodInteractor;
    }

    /**
     * Executes the search food use case.
     * @param searchQuery the food name to search with
     */
    public void execute(String searchQuery) {
        searchFoodInteractor.execute(new SearchFoodInputData(searchQuery));
    }
}
