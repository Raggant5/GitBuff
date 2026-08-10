package use_case.nutrition.meal.get_meals;

import java.util.List;

import use_case.nutrition.meal.MealData;

/**
 * Output Data for the Get Meals Use Case.
 */
public class GetMealsOutputData {

    private final List<MealData> meals;

    public GetMealsOutputData(final List<MealData> meals) {
        this.meals = meals;
    }

    public List<MealData> getMeals() {
        return this.meals;
    }
}
