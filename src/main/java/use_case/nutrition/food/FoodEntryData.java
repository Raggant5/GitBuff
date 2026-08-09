package use_case.nutrition.food;

import entity.FoodUnit;

public class FoodEntryData {

    private final Integer id;
    private final String foodName;
    private final FoodNutritionData nutrition;
    private final double quantity;
    private final FoodUnit unit;
    private final double grams;

    public FoodEntryData(Integer id, String foodName, FoodNutritionData nutrition, double quantity, FoodUnit unit,
                         double grams) {
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

    public FoodNutritionData getNutrition() {
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
