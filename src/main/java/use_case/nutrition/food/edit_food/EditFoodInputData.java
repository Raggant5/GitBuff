package use_case.nutrition.food.edit_food;

import entity.FoodUnit;
import use_case.nutrition.food.FoodNutritionInput;

public class EditFoodInputData {

    private final Integer id;
    private final String foodName;
    private final FoodNutritionInput nutrition;
    private final String quantity;
    private final FoodUnit unit;
    private final String grams;

    public EditFoodInputData(
            Integer id,
            String foodName,
            FoodNutritionInput nutrition,
            String quantity,
            FoodUnit unit,
            String grams
    ) {
        this.id = id;
        this.foodName = foodName;
        this.nutrition = nutrition;
        this.quantity = quantity;
        this.unit = unit;
        this.grams = grams;
    }

    public Integer getId() {
        return id;
    }

    public String getFoodName() {
        return foodName;
    }

    public FoodNutritionInput getNutrition() {
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
