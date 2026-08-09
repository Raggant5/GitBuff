package use_case.nutrition.meal.edit_meal;

import java.time.LocalDate;
import java.util.List;

import use_case.nutrition.food.FoodEntryData;

public class EditMealOutputData {

    private final int id;
    private final LocalDate date;
    private final String name;
    private final List<FoodEntryData> foodEntries;

    public EditMealOutputData(int id, LocalDate date, String name, List<FoodEntryData> foodEntries) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.foodEntries = foodEntries;
    }

    public int getId() {
        return id;
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
