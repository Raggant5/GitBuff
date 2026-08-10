package use_case.nutrition.food.search_food;

import java.util.List;

public class SearchFoodOutputData {
    private final List<FoodSearchResultData> foodResults;

    public SearchFoodOutputData(List<FoodSearchResultData> foodResults) {
        this.foodResults = foodResults;
    }

    public List<FoodSearchResultData> getFoodResults() {
        return foodResults;
    }
}
