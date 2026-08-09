package use_case.nutrition.meal.prepare_edit_meal;

import java.time.LocalDate;
import java.util.List;

import use_case.nutrition.food.FoodEntryData;

public class PrepareEditMealOutputData {

    private final int mealId;
    private final LocalDate date;
    private final String name;
    private final List<FoodEntryData> foodEntries;

    public PrepareEditMealOutputData(int mealId, LocalDate date, String name, List<FoodEntryData> foodEntries) {
        this.mealId = mealId;
        this.date = date;
        this.name = name;
        this.foodEntries = foodEntries;
    }

    public int getMealId() {
        return mealId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public List<FoodEntryData> getFoodEntries() {
        return foodEntries;
    }
}
