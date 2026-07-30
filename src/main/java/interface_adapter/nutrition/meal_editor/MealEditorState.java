package interface_adapter.nutrition.meal_editor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import entity.Meal;

/**
 * The state for the Add FoodEntry View Model / Add Meal Use Case.
 */
public class MealEditorState {

    private Meal editingMeal;
    private LocalDate date;
    private String name = "";
    private List<FoodEntry> foodEntriesForMeal = new ArrayList<>();
    private String errorMessage = "";
    private Boolean showFoodEditor = false;

    public List<FoodEntry> getFoodEntriesForMeal() {
        return foodEntriesForMeal;
    }

    public void addFoodEntry(FoodEntry foodEntry) {
        foodEntriesForMeal.add(foodEntry);
    }

    public void setFoodEntriesForMeal(List<FoodEntry> foodEntriesForMeal) {
        this.foodEntriesForMeal = foodEntriesForMeal;
    }

    public void reset() {
        editingMeal = null;
        date = null;
        name = "";
        foodEntriesForMeal = new ArrayList<>();
        errorMessage = "";
    }

    public boolean getShowFoodEditor() {
        return showFoodEditor;
    }

    public void setShowFoodEditor(boolean showFoodEditor) {
        this.showFoodEditor = showFoodEditor;
    }

    public Meal getEditingMeal() {
        return editingMeal;
    }

    public void setEditingMeal(Meal editingMeal) {
        this.editingMeal = editingMeal;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
