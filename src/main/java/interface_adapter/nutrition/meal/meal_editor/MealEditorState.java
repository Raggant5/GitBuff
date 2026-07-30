package interface_adapter.nutrition.meal.meal_editor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;

/**
 * The state for the Add FoodEntry View Model / Add Meal Use Case.
 */
public class MealEditorState {
    private Integer id;
    private LocalDate date;
    private String name = "";
    private List<FoodEntry> foodEntriesForMeal = new ArrayList<>();

    public List<FoodEntry> getFoodEntriesForMeal() {
        return foodEntriesForMeal;
    }

    public void setFoodEntriesForMeal(List<FoodEntry> foodEntriesForMeal) {
        this.foodEntriesForMeal = foodEntriesForMeal;
    }

    public void reset() {
        id = null;
        date = null;
        name = "";
        foodEntriesForMeal = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
