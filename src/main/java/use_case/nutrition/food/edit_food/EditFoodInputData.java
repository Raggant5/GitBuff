package use_case.nutrition.food.edit_food;

import entity.FoodEntry;
import entity.FoodNutrition;
import entity.FoodUnit;

public class EditFoodInputData {

    private final FoodEntry foodEntry;
    private final String foodName;
    private final FoodNutrition nutrition;
    private final double quantity;
    private final FoodUnit unit;
    private final double grams;

    public EditFoodInputData(
            FoodEntry foodEntry,
            String foodName,
            FoodNutrition nutrition,
            double quantity,
            FoodUnit unit,
            double grams
    ) {
        this.foodEntry = foodEntry;
        this.foodName = foodName;
        this.nutrition = nutrition;
        this.quantity = quantity;
        this.unit = unit;
        this.grams = grams;
    }

    public FoodEntry getFoodEntry() {
        return foodEntry;
    }

    public String getFoodName() {
        return foodName;
    }

    public FoodNutrition getNutrition() {
        return nutrition;
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
