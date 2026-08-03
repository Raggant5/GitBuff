package use_case.nutrition.food.search_food;

import java.util.List;

import entity.FoodSearchResult;

public class SearchFoodOutputData {
    private final List<FoodSearchResult> foodResults;

    public SearchFoodOutputData(List<FoodSearchResult> foodResults) {
        this.foodResults = foodResults;
    }

    public List<FoodSearchResult> getFoodResults() {
        return foodResults;
    }
}
