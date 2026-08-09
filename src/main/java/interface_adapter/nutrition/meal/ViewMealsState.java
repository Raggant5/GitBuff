package interface_adapter.nutrition.meal;

import interface_adapter.nutrition.food.FoodEntryDisplayData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewMealsState {

    private List<MealDisplayData> meals = new ArrayList<>();
    private String error = "";

    public List<MealDisplayData> getMeals() {
        return meals;
    }

    public void setMeals(List<MealDisplayData> meals) {
        this.meals = meals;
    }

    /**
     * Adds a meal to display to the existing list.
     * @param meal the meal display data to add
     */
    public void addMeal(MealDisplayData meal) {
        this.meals.add(meal);
    }

    /**
     * Replace existing meal in the list.
     * @param meal the meal display data to replace existing list with matching id with
     */
    public void replaceMeal(MealDisplayData meal) {
        for (int i = 0; i < meals.size(); i++) {
            if (Objects.equals(meals.get(i).getId(), meal.getId())) {
                meals.set(i, meal);
                break;
            }
        }
    }

    /**
     * Removes the meal from the history if it matches the meal id.
     * @param mealId meal id of meal to be removed.
     */
    public void removeMeal(int mealId) {
        meals.removeIf(meal -> meal.getId() == mealId);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
