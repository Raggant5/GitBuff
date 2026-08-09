package data_access;

import java.util.ArrayList;
import java.util.List;

import entity.FoodSearchResult;
import use_case.nutrition.food.search_food.SearchFoodDataAccessInterface;

/**
 * Mock implementation returning results instead of calling the real nutrition API.
 */
public class MockSearchFoodDataAccessObject implements SearchFoodDataAccessInterface {

    private static final List<FoodSearchResult> MOCK_RESULTS = List.of(
            new FoodSearchResult("Apple", "1 Medium Apple", 182, 94.6, 0.5, 21.1, 0.5),
            new FoodSearchResult("Apple, Raw", "1 Large Apple", 182, 111, 0.3, 26.9, 0.3),
            new FoodSearchResult("Apple Cider", "1 Cup", 244, 112.2, 0.2, 27.6, 0.3),
            new FoodSearchResult("Apple Nectar", "1 Small Apple", 182, 81.7, 0.3, 19.8, 0.1),
            new FoodSearchResult("Apple Tart", "1 Medium Apple", 182, 413.1, 4.6, 59.5, 16)
    );

    @Override
    public List<FoodSearchResult> searchFood(String searchQuery) {
        return new ArrayList<>(MOCK_RESULTS);
    }
}
