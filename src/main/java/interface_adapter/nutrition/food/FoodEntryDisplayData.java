package interface_adapter.nutrition.food;

/**
 * DTO for FoodEntry, so the View/Interface Adapter never holds entities.
 */
public class FoodEntryDisplayData {

    private final Integer id;
    private final String foodName;
    private final FoodNutritionDisplayData nutrition;
    private final double quantity;
    private final FoodUnitOption unit;
    private final double grams;

    public FoodEntryDisplayData(Integer id, String foodName, FoodNutritionDisplayData nutrition, double quantity,
                                FoodUnitOption unit, double grams) {
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

    public FoodUnitOption getUnit() {
        return unit;
    }

    public double getGrams() {
        return grams;
    }
}
