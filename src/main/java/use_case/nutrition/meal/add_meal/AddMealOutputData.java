package use_case.nutrition.meal.add_meal;

import java.time.LocalDate;
import java.util.List;

import use_case.nutrition.food.FoodEntryData;

/**
 * Output Data for the Add Meal Use Case.
 */
public class AddMealOutputData {

    private final int id;
    private final String userId;
    private final LocalDate date;
    private final String name;
    private final List<FoodEntryData> foodEntries;

    public AddMealOutputData(int id, String userId, LocalDate date, String name, List<FoodEntryData> foodEntries) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.name = name;
        this.foodEntries = foodEntries;
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
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
