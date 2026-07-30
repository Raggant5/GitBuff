package interface_adapter.nutrition.meals;

import java.util.ArrayList;
import java.util.List;

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
