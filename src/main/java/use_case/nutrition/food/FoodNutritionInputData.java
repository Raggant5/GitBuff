package use_case.nutrition.food;

public class FoodNutritionInputData {

    private final String calories;
    private final String protein;
    private final String carbs;
    private final String fat;

    public FoodNutritionInputData(
            String calories,
            String protein,
            String carbs,
            String fat
    ) {
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
