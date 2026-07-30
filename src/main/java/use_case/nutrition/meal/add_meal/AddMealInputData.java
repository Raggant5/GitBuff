package use_case.nutrition.meal.add_meal;

import java.time.LocalDate;
import java.util.List;

import entity.FoodEntry;

public class AddMealInputData {

    private final String name;
    private final String userId;
    private final LocalDate date;
    private final List<FoodEntry> foodEntriesForMeal;

    public AddMealInputData(String name, String userId, LocalDate date, List<FoodEntry> foodEntriesForMeal) {
        this.name = name;
        this.userId = userId;
        this.date = date;
        this.foodEntriesForMeal = foodEntriesForMeal;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public List<FoodEntry> getFoodEntries() {
        return foodEntriesForMeal;
    }

}
