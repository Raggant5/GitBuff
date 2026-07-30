package interface_adapter.nutrition.food_editor;

import entity.FoodEntry;
import entity.FoodUnit;
import use_case.nutrition.food.edit_food.EditFoodInputBoundary;
import use_case.nutrition.food.edit_food.EditFoodInputData;
import use_case.nutrition.food.FoodNutritionInputData;

public class EditFoodController {

    private final EditFoodInputBoundary editFoodInteractor;

    public EditFoodController(EditFoodInputBoundary editFoodInteractor) {
        this.editFoodInteractor = editFoodInteractor;
    }

    public void execute(FoodEntry food, String foodName, FoodNutritionInputData nutrition, String quantity,
                        FoodUnit unit,
                        String grams) {
        editFoodInteractor.execute(new EditFoodInputData(food, foodName, nutrition, quantity, unit, grams));
    }

    public void switchToEdit(FoodEntry food) {

    }
}
