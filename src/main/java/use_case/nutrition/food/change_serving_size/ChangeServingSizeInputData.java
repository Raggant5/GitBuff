package use_case.nutrition.food.change_serving_size;

import entity.FoodUnit;

public class ChangeServingSizeInputData {

    private final FoodUnit unit;
    private final double originalServingGrams;
    private final double servingGrams;
    private final double servingCalories;
    private final double servingProtein;
    private final double servingCarbs;
    private final double servingFat;

    public ChangeServingSizeInputData(
            FoodUnit unit,
            double originalServingGrams,
            double servingGrams,
            double servingCalories,
            double servingProtein,
            double servingCarbs,
            double servingFat
    ) {
        this.unit = unit;
        this.originalServingGrams = originalServingGrams;
        this.servingGrams = servingGrams;
        this.servingCalories = servingCalories;
        this.servingProtein = servingProtein;
        this.servingCarbs = servingCarbs;
        this.servingFat = servingFat;
    }

    public FoodUnit getUnit() {
        return unit;
    }

    public double getOriginalServingGrams() {
        return originalServingGrams;
    }

    public double getServingGrams() {
        return servingGrams;
    }

    public double getServingCalories() {
        return servingCalories;
    }

    public double getServingProtein() {
        return servingProtein;
    }

    public double getServingCarbs() {
        return servingCarbs;
    }

    public double getServingFat() {
        return servingFat;
    }
}
