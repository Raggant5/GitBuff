package interface_adapter.nutrition.meal;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.nutrition.food.FoodEntryDisplayData;
import use_case.nutrition.food.FoodEntryInputData;
import use_case.nutrition.food.FoodNutritionInput;
import use_case.nutrition.meal.edit_meal.EditMealInputBoundary;
import use_case.nutrition.meal.edit_meal.EditMealInputData;

public class EditMealController {

    private final EditMealInputBoundary editMealInteractor;

    public EditMealController(EditMealInputBoundary editMealInteractor) {
        this.editMealInteractor = editMealInteractor;
    }

    /**
     * Executes the Edit Meal Use Case.
     * @param mealId the id of the meal being edited
     * @param name the updated meal name
     * @param foodEntries the updated list of food entries
     * @param foodEntryIdsToDelete ids of food entries to delete
     */
    public void execute(int mealId, String name, List<FoodEntryDisplayData> foodEntries,
                        List<Integer> foodEntryIdsToDelete) {
        final List<FoodEntryInputData> foods = new ArrayList<>();
        for (FoodEntryDisplayData food : foodEntries) {
            final FoodNutritionInput nutrition = new FoodNutritionInput(food.getNutrition().getCalories(),
                    food.getNutrition().getProtein(), food.getNutrition().getCarbs(), food.getNutrition().getFat());
            foods.add(new FoodEntryInputData(food.getId(), food.getFoodName(), nutrition, food.getQuantity(),
                    food.getUnit(), food.getGrams()));
        }
        editMealInteractor.execute(new EditMealInputData(mealId, name, foods, foodEntryIdsToDelete));
    }

}
