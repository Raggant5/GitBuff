package interface_adapter.nutrition.food;

import entity.FoodUnit;

public class FoodState {
    private Integer id;
    private String foodName = "";
    private String calories = "";
    private String protein = "";
    private String carbs = "";
    private String fat = "";
    private String quantity = "";
    private String grams = "";
    private FoodUnit unit = FoodUnit.GRAM;

    private String error = "";

    public void reset() {
        id = null;
        foodName = "";
        calories = "";
        protein = "";
        carbs = "";
        fat = "";
        quantity = "";
        grams = "";
        unit = FoodUnit.GRAM;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGrams() {
        return grams;
    }

    public void setGrams(String grams) {
        this.grams = grams;
    }

    public FoodUnit getUnit() {
        return unit;
    }

    public void setUnit(FoodUnit unit) {
        this.unit = unit;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
