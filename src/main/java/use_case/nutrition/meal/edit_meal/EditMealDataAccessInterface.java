package use_case.nutrition.meal.edit_meal;

import entity.FoodEntry;
import entity.Meal;

public interface EditMealDataAccessInterface {

    void editMeal(Meal meal);

    int saveFoodEntry(FoodEntry foodEntry);

    void editFoodEntry(FoodEntry foodEntry);
}