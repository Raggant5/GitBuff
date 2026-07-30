package use_case.nutrition.food.edit_food;

import entity.FoodEntry;
import entity.FoodUnit;
import use_case.nutrition.food.FoodNutritionInputData;

public class EditFoodInputData {

    private final FoodEntry foodEntry;
    private final String foodName;
    private final FoodNutritionInputData nutrition;
    private final String quantity;
    private final FoodUnit unit;
    private final String grams;

    public EditFoodInputData(
            FoodEntry foodEntry,
            String foodName,
            FoodNutritionInputData nutrition,
            String quantity,
            FoodUnit unit,
            String grams
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

    public FoodNutritionInputData getNutrition() {
        return nutrition;
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
