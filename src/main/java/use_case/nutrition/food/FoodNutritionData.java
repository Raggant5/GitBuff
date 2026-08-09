package use_case.nutrition.food;

/**
 * Use case boundary DTO for the validated calories/protein/carbs/fat of a food entry.
 * Used for InputData/OutputData everywhere except the Add/Edit Food validation boundary -
 * by the time data reaches this type it's already known-valid.
 */
public class FoodNutritionData {

    private final double calories;
    private final double protein;
    private final double carbs;
    private final double fat;

    public FoodNutritionData(double calories, double protein, double carbs, double fat) {
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
