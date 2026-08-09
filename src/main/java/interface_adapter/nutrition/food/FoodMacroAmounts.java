package interface_adapter.nutrition.food;

/**
 * Computable calories/protein/carbs/fat for food. Unlike FoodNutritionDisplayData,
 * these values aren't bound to editable text fields but for calculation.
 */
public class FoodMacroAmounts {

    private final double calories;
    private final double protein;
    private final double carbs;
    private final double fat;

    public FoodMacroAmounts(double calories, double protein, double carbs, double fat) {
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFat() {
        return fat;
    }
}
