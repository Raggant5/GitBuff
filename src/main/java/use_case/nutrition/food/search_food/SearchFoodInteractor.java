package use_case.nutrition.food.search_food;

import java.io.IOException;
import java.util.List;

import entity.FoodSearchResult;

public class SearchFoodInteractor implements SearchFoodInputBoundary {

    private final SearchFoodDataAccessInterface searchFoodDataAccess;
    private final SearchFoodOutputBoundary searchFoodPresenter;

    public SearchFoodInteractor(SearchFoodDataAccessInterface searchFoodDataAccess,
            SearchFoodOutputBoundary searchFoodPresenter) {
        this.searchFoodDataAccess = searchFoodDataAccess;
        this.searchFoodPresenter = searchFoodPresenter;
    }

    @Override
    public void execute(SearchFoodInputData searchFoodInputData) {
        try {
            final List<FoodSearchResult> foodResults = searchFoodDataAccess.searchFood(
                    searchFoodInputData.getSearchQuery());
            searchFoodPresenter.prepareSuccessView(new SearchFoodOutputData(foodResults));
        }
        catch (IOException exception) {
            searchFoodPresenter.prepareFailView(exception.getMessage());
        }
    }
}
