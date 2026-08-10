package use_case.nutrition.food;

/**
 * Groups the raw, unvalidated calories/protein/carbs/fat form strings for a food entry.
 * Only used at the Add/Edit Food boundary, where the interactor validates each field and
 * reports per-field errors - everywhere else already-valid data flows as FoodNutritionData.
 */
public class FoodNutritionInput {

    private final String calories;
    private final String protein;
    private final String carbs;
    private final String fat;

    public FoodNutritionInput(String calories, String protein, String carbs, String fat) {
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
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
