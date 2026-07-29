package use_case.nutrition.food;

import entity.FoodNutrition;
import entity.FoodUnit;

public class AddFoodEntryInputData {

    private final String foodName;
    private final FoodNutrition foodNutrition;
    private final double quantity;
    private final FoodUnit unit;
    private final double grams;

    public AddFoodEntryInputData(String foodName, FoodNutrition foodNutrition, double quantity,
                                 FoodUnit unit, double grams) {
        this.foodName = foodName;
        this.foodNutrition = foodNutrition;
        this.quantity = quantity;
        this.unit = unit;
        this.grams = grams;
    }

    public String getFoodName() {
        return foodName;
    }

    public FoodNutrition getFoodNutrition() {
        return foodNutrition;
    }

    public double getQuantity() {
        return quantity;
    }

    public FoodUnit getUnit() {
        return unit;
    }

    public double getGrams() {
        return grams;
    }

}
