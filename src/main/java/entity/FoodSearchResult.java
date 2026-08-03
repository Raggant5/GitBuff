package entity;

public class FoodSearchResult {
    private final String name;
    private final String servingLabel;
    private final double servingGrams;
    private final double calories;
    private final double protein;
    private final double carbs;
    private final double fat;

    public FoodSearchResult(String name, String servingLabel, double servingGrams, double calories,
                            double protein, double carbs, double fat) {
        this.name = name;
        this.servingLabel = servingLabel;
        this.servingGrams = servingGrams;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public String getFoodName() {
        return name;
    }

    public String getServingLabel() {
        return servingLabel;
    }

    public double getServingGrams() {
        return servingGrams;
    }

    public double getServingCalories() {
        return calories;
    }

    public double getServingProtein() {
        return protein;
    }

    public double getServingCarbs() {
        return carbs;
    }

    public double getServingFat() {
        return fat;
    }
}
