package use_case.nutrition.meal.edit_meal;

import java.util.List;

import entity.FoodEntry;
import entity.Meal;

public class EditMealInputData {

    private final Meal meal;
    private final String name;
    private final List<FoodEntry> foodEntries;

    public EditMealInputData(Meal meal, String name, List<FoodEntry> foodEntries) {
        this.meal = meal;
        this.name = name;
        this.foodEntries = foodEntries;
    }

    public Meal getMeal() {
        return meal;
    }

    public String getName() {
        return name;
    }

    public List<FoodEntry> getFoodEntries() {
        return foodEntries;
    }
}
