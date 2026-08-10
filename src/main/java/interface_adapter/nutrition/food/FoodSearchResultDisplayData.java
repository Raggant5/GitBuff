package interface_adapter.nutrition.food;

/**
 * DTO for a food search result, so panels/state in this layer don't need to depend
 * on the use case's FoodSearchResultData directly.
 */
public class FoodSearchResultDisplayData {

    private final String foodName;
    private final String servingLabel;
    private final double servingGrams;
    private final FoodMacroAmounts nutrition;
    private final FoodUnitOption unit;
    private final double quantity;

    public FoodSearchResultDisplayData(
            String foodName,
            String servingLabel,
            double servingGrams,
            FoodMacroAmounts nutrition,
            FoodUnitOption unit,
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

    public FoodMacroAmounts getNutrition() {
        return nutrition;
    }

    public FoodUnitOption getUnit() {
        return unit;
    }

    public double getQuantity() {
        return quantity;
    }
}
