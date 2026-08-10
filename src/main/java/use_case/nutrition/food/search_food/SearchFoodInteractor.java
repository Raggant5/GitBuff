package use_case.nutrition.food.search_food;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.FoodSearchResult;
import use_case.nutrition.food.FoodNutritionData;

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
            final List<FoodSearchResultData> foodResultsData = new ArrayList<>();
            for (FoodSearchResult food : foodResults) {
                final FoodNutritionData nutrition = new FoodNutritionData(food.getServingCalories(),
                        food.getServingProtein(), food.getServingCarbs(), food.getServingFat());
                foodResultsData.add(new FoodSearchResultData(food.getFoodName(), food.getServingLabel(),
                        food.getServingGrams(), nutrition, food.getUnit(), food.getQuantity()));
            }
            searchFoodPresenter.prepareSuccessView(new SearchFoodOutputData(foodResultsData));
        }
        catch (IOException exception) {
            searchFoodPresenter.prepareFailView(exception.getMessage());
        }
    }
}
