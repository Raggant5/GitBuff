package interface_adapter.nutrition.meal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import entity.FoodEntryFactory;
import entity.Meal;

/**
 * The state for the Add FoodEntry View Model / Add Meal Use Case.
 */
public class MealEditorState {

    private Meal editingMeal;
    private LocalDate date;
    private String name = "";
    private List<FoodEntry> foodEntriesForMeal = new ArrayList<>();
    private List<FoodEntry> foodEntriesDeleteStage = new ArrayList<>();
    private String errorMessage = "";
    private Boolean showFoodEditor = false;

    public List<FoodEntry> getFoodEntriesForMeal() {
        return foodEntriesForMeal;
    }

    /**
     * Adding another food entry into the list of foods associated with the meal.
     * @param foodEntry the food to be added
     */
    public void addFoodEntry(FoodEntry foodEntry) {
        foodEntriesForMeal.add(foodEntry);
    }

    public void setFoodEntriesForMeal(List<FoodEntry> foodEntriesForMeal) {
        this.foodEntriesForMeal = foodEntriesForMeal;
    }

    /**
     * Remove the given food entry from the meal being edited.
     * @param foodEntry the food entry to be removed
     */
    public void removeFoodEntry(FoodEntry foodEntry) {
        foodEntriesForMeal.remove(foodEntry);
    }

    public List<FoodEntry> getFoodEntriesDeleteStage() {
        return foodEntriesDeleteStage;
    }

    /**
     * Adds a food entry to be deleted upon saving a meal.
     * @param foodEntry food entry to be deleted upon saving a meal
     */
    public void addFoodEntryToBeDeleted(FoodEntry foodEntry) {
        foodEntriesDeleteStage.add(foodEntry);
    }

    /**
     * Removes a food from being deleted within the meal editing stage.
     * @param foodEntry food entry to be undeleted
     */
    public void removeFoodEntryFromDeletion(FoodEntry foodEntry) {
        foodEntriesDeleteStage.remove(foodEntry);
    }

    /**
     * Setting values back to their defaults as to reuse the object for more adding/editing meals.
     */
    public void reset() {
        editingMeal = null;
        date = null;
        name = "";
        foodEntriesForMeal = new ArrayList<>();
        errorMessage = "";
        showFoodEditor = false;
        foodEntriesDeleteStage = new ArrayList<>();
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
