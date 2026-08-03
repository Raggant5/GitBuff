package use_case.nutrition.food.search_food;

import entity.FoodUnit;

public class SelectSearchResultFoodOutputData {

    private final String calories;
    private final String protein;
    private final String carbs;
    private final FoodUnit unit;

    public SelectSearchResultFoodOutputData(String calories, String protein, String carbs, FoodUnit unit) {
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.unit = unit;
    }

    public String getCalories() {
        return calories;
    }

    public String getProtein() {
        return protein;
    }

    public String getCarbs() {
        return carbs;
    }

    public FoodUnit getUnit() {
        return unit;
    }
}
