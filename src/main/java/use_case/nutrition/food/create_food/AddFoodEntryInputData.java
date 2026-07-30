package use_case.nutrition.food.create_food;

import entity.FoodUnit;
import use_case.nutrition.food.FoodNutritionInputData;

public class AddFoodEntryInputData {

    private final String foodName;
    private final FoodNutritionInputData foodNutrition;
    private final String quantity;
    private final FoodUnit unit;
    private final String grams;

    public AddFoodEntryInputData(String foodName, FoodNutritionInputData foodNutrition, String quantity,
                                 FoodUnit unit, String grams) {
        this.foodName = foodName;
        this.foodNutrition = foodNutrition;
        this.quantity = quantity;
        this.unit = unit;
        this.grams = grams;
    }

    public String getFoodName() {
        return foodName;
    }

    public FoodNutritionInputData getFoodNutrition() {
        return foodNutrition;
    }

    public String getQuantity() {
        return quantity;
    }

    public FoodUnit getUnit() {
        return unit;
    }

    public String getGrams() {
        return grams;
    }

}
