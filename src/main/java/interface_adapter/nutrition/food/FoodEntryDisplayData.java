package interface_adapter.nutrition.food;

import entity.FoodUnit;

/**
 * DTO for FoodEntry, so the View/Interface Adapter never holds entities.
 */
public class FoodEntryDisplayData {

    private final Integer id;
    private final String foodName;
    private final FoodNutritionDisplayData nutrition;
    private final double quantity;
    private final FoodUnit unit;
    private final double grams;

    public FoodEntryDisplayData(Integer id, String foodName, FoodNutritionDisplayData nutrition, double quantity,
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

    public FoodNutritionDisplayData getNutrition() {
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
