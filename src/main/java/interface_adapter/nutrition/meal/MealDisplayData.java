package interface_adapter.nutrition.meal;

import java.time.LocalDate;
import java.util.List;

import interface_adapter.nutrition.food.FoodEntryDisplayData;

/**
 * Meal DTO, for use in State/View. Never holds an Entity.
 */
public class MealDisplayData {

    private final int id;
    private final LocalDate date;
    private final String name;
    private final List<FoodEntryDisplayData> foodEntries;

    public MealDisplayData(int id, LocalDate date, String name, List<FoodEntryDisplayData> foodEntries) {
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

    public List<FoodEntryDisplayData> getFoodEntries() {
        return foodEntries;
    }
}
