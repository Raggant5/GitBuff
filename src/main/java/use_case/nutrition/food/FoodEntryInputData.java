package use_case.nutrition.food;

import entity.FoodUnit;

/**
 * Input-side list item for a food entry within a meal being saved - raw strings, the
 * interactor parses when building the entity. Mirrors FoodEntryData, which carries the
 * validated numeric form back out.
 */
public class FoodEntryInputData {

    private final Integer id;
    private final String foodName;
    private final FoodNutritionInput nutrition;
    private final double quantity;
    private final FoodUnit unit;
    private final double grams;

    public FoodEntryInputData(Integer id, String foodName, FoodNutritionInput nutrition, double quantity,
                              FoodUnit unit, double grams) {
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

    public String getCalories() {
        return nutrition.getCalories();
    }

    public String getProtein() {
        return nutrition.getProtein();
    }

    public String getCarbs() {
        return nutrition.getCarbs();
    }

    public String getFat() {
        return nutrition.getFat();
    }

    public double getQuantity() {
        return quantity;
    }

    public FoodUnit getUnit() {
        return unit;
    }

    public double getGrams() {
        return grams;
    }
}
