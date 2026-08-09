package use_case.nutrition.food.search_food;

import entity.FoodUnit;
import use_case.nutrition.food.FoodNutritionData;

public class FoodSearchResultData {

    private final String foodName;
    private final String servingLabel;
    private final double servingGrams;
    private final FoodNutritionData nutrition;
    private final FoodUnit unit;
    private final double quantity;

    public FoodSearchResultData(
            String foodName,
            String servingLabel,
            double servingGrams,
            FoodNutritionData nutrition,
            FoodUnit unit,
            double quantity
    ) {
        this.foodName = foodName;
        this.servingLabel = servingLabel;
        this.servingGrams = servingGrams;
        this.nutrition = nutrition;
        this.unit = unit;
        this.quantity = quantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getServingLabel() {
        return servingLabel;
    }

    public double getServingGrams() {
        return servingGrams;
    }

    public FoodNutritionData getNutrition() {
        return nutrition;
    }

    public FoodUnit getUnit() {
        return unit;
    }

    public double getQuantity() {
        return quantity;
    }
}
