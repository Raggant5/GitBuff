package use_case.nutrition.food.search_food;

import java.util.List;

import entity.FoodSearchResult;
import interface_adapter.nutrition.food.SearchFoodPresenter;

public class SearchFoodInteractor implements SearchFoodInputBoundary {

    private final SearchFoodDataAccessInterface searchFoodDataAccess;
    private final SearchFoodPresenter searchFoodPresenter;

    public SearchFoodInteractor(SearchFoodDataAccessInterface searchFoodDataAccess,
            SearchFoodPresenter searchFoodPresenter) {
        this.searchFoodDataAccess = searchFoodDataAccess;
        this.searchFoodPresenter = searchFoodPresenter;
    }

    @Override
    public void execute(SearchFoodInputData searchFoodInputData) {
        try {
            final List<FoodSearchResult> foodResults = searchFoodDataAccess.searchFood(searchFoodInputData.getSearchQuery());

            searchFoodPresenter.prepareSuccessView(new SearchFoodOutputData(foodResults));
        }
        catch (Exception exception) {
            searchFoodPresenter.prepareFailView(exception.getMessage());
        }
    }
}