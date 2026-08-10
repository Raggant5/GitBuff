package entity;

/**
 * Represents the nutritional information of food.
 */
public class FoodNutrition {

    private double calories;
    private double protein;
    private double carbs;
    private double fat;

    public FoodNutrition(double calories, double protein,
                         double carbs, double fat) {
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    /**
     * Scales all nutrition values by the given ratio, e.g. when the serving size changes.
     * @param ratio the factor to scale calories, protein, carbs, and fat by
     * @return a new FoodNutrition with each value multiplied by ratio
     */
    public FoodNutrition scaledTo(double ratio) {
        return new FoodNutrition(calories * ratio, protein * ratio, carbs * ratio, fat * ratio);
    }
}
