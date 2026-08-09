package use_case.nutrition.food.prepare_edit_food;

import entity.FoodUnit;
import use_case.nutrition.food.FoodNutritionInput;

public class PrepareEditFoodInputData {

    private final Integer id;
    private final String foodName;
    private final FoodNutritionInput nutrition;
    private final double quantity;
    private final FoodUnit unit;
    private final double grams;

    public PrepareEditFoodInputData(Integer id, String foodName, FoodNutritionInput nutrition, double quantity,
                                    FoodUnit unit, double grams) {
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
