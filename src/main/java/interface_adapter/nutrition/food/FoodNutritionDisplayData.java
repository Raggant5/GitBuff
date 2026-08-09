package interface_adapter.nutrition.food;

/**
 * View-facing calories/protein/carbs/fat for a food entry, so panels/state in this
 * layer don't need to depend on the use case's FoodNutritionData directly.
 */
public class FoodNutritionDisplayData {

    private final String calories;
    private final String protein;
    private final String carbs;
    private final String fat;

    public FoodNutritionDisplayData(String calories, String protein, String carbs, String fat) {
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    /**
     * Formats validated numeric output data for display.
     */
    public FoodNutritionDisplayData(double calories, double protein, double carbs, double fat) {
        this(String.valueOf(calories), String.valueOf(protein), String.valueOf(carbs), String.valueOf(fat));
    }

    public String getCalories() {
        return calories;
    }

    public String getProtein() {
        return protein;
    }

    public String getCarbs() {
        return carbs;
    }

    public String getFat() {
        return fat;
    }
}
