package interface_adapter.nutrition.meal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import interface_adapter.nutrition.food.FoodEntryDisplayData;

/**
 * The state for the Add FoodEntry View Model / Add Meal Use Case.
 */
public class MealEditorState {

    private Integer editingMealId;
    private LocalDate date;
    private String name = "";
    private List<FoodEntryDisplayData> foodEntriesForMeal = new ArrayList<>();
    private List<Integer> foodEntriesDeleteStage = new ArrayList<>();
    private String errorMessage = "";
    private Boolean showFoodEditor = false;
    private int nextTempId = -1;

    public List<FoodEntryDisplayData> getFoodEntriesForMeal() {
        return foodEntriesForMeal;
    }

    /**
     * Adding another food entry into the list of foods associated with the meal.
     * @param foodEntry the food to be added
     */
    public void addFoodEntry(FoodEntryDisplayData foodEntry) {
        foodEntriesForMeal.add(foodEntry);
    }

    public int getNextTempId() {
        return nextTempId;
    }

    public void setNextTempId(int nextTempId) {
        this.nextTempId = nextTempId;
    }

    public void setFoodEntriesForMeal(List<FoodEntryDisplayData> foodEntriesForMeal) {
        this.foodEntriesForMeal = foodEntriesForMeal;
    }

    /**
     * Removes the food entry with the given id from the meal being edited.
     * @param id the id of the food entry to be removed
     */
    public void removeFoodEntryById(Integer id) {
        foodEntriesForMeal.removeIf(foodEntry -> Objects.equals(foodEntry.getId(), id));
    }

    /**
     * Replaces the food entry matching the given food entry's id with the given food entry.
     * @param foodEntry the updated food entry
     */
    public void replaceFoodEntry(FoodEntryDisplayData foodEntry) {
        for (int i = 0; i < foodEntriesForMeal.size(); i++) {
            if (Objects.equals(foodEntriesForMeal.get(i).getId(), foodEntry.getId())) {
                foodEntriesForMeal.set(i, foodEntry);
                break;
            }
        }
    }

    public List<Integer> getFoodEntriesDeleteStage() {
        return foodEntriesDeleteStage;
    }

    /**
     * Marks the food entry with the given id to be deleted upon saving a meal.
     * @param id id of the food entry to be deleted upon saving a meal
     */
    public void addFoodEntryToBeDeleted(Integer id) {
        foodEntriesDeleteStage.add(id);
    }

    /**
     * Setting values back to their defaults as to reuse the object for more adding/editing meals.
     */
    public void reset() {
        editingMealId = null;
        date = null;
        name = "";
        foodEntriesForMeal = new ArrayList<>();
        errorMessage = "";
        showFoodEditor = false;
        foodEntriesDeleteStage = new ArrayList<>();
        nextTempId = -1;
    }

    public boolean getShowFoodEditor() {
        return showFoodEditor;
    }

    public void setShowFoodEditor(boolean showFoodEditor) {
        this.showFoodEditor = showFoodEditor;
    }

    public Integer getEditingMealId() {
        return editingMealId;
    }

    public void setEditingMealId(Integer editingMealId) {
        this.editingMealId = editingMealId;
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
