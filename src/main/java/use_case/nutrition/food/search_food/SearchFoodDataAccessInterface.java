package use_case.nutrition.food.search_food;

import java.io.IOException;
import java.util.List;

import entity.FoodSearchResult;

public interface SearchFoodDataAccessInterface {

    /**
     * Returns a list of foods given a food name.
     * @param searchQuery the name to query the API with for food names
     * @return a list of food results from API
     */
    List<FoodSearchResult> searchFood(String searchQuery) throws IOException;
}
