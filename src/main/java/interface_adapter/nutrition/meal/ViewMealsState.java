package interface_adapter.nutrition.meal;

import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import entity.Meal;

public class ViewMealsState {

    private List<Meal> meals = new ArrayList<>();
    private String error = "";

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    /**
     * Removes the meal from the history if it matches the meal id.
     * @param mealId meal id of meal to be removed.
     */
    public void removeMeal(int mealId) {
        meals.removeIf(meal -> meal.getId() == mealId);
    }

    /**
     * Removes the given food entry from whichever meal contains it.
     * @param foodEntry food entry to be removed.
     */
    public void removeFoodEntry(FoodEntry foodEntry) {
        for (Meal meal : meals) {
            meal.removeFoodEntry(foodEntry);
        }
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
