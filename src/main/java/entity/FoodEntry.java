package entity;

/**
 * The representation of a food/ingredient within a meal in our program.
 */
public class FoodEntry {

    private Integer id;
    private Integer mealId;
    private String foodName;
    private FoodNutrition nutrition;
    private double quantity;
    private FoodUnit unit;
    private double grams;

    public FoodEntry(String foodName, FoodNutrition nutrition, double quantity, FoodUnit unit, double grams) {
        this.foodName = foodName;
        this.nutrition = nutrition;
        this.quantity = quantity;
        this.unit = unit;
        this.grams = grams;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getMealId() {
        return mealId;
    }

    public void setMealId(Integer mealId) {
        this.mealId = mealId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public FoodNutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(FoodNutrition nutrition) {
        this.nutrition = nutrition;
    }

    public double getGrams() {
        return grams;
    }

    public void setGrams(double grams) {
        this.grams = grams;
    }

    public FoodUnit getUnit() {
        return unit;
    }

    public void setUnit(FoodUnit unit) {
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

}
