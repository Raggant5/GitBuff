package interface_adapter.nutrition.food_editor;

import entity.FoodEntry;
import entity.FoodNutrition;
import entity.FoodUnit;
import use_case.nutrition.food.edit_food.EditFoodInputBoundary;
import use_case.nutrition.food.edit_food.EditFoodInputData;

public class EditFoodController {

    private final EditFoodInputBoundary editFoodInteractor;

    public EditFoodController(EditFoodInputBoundary editFoodInteractor) {
        this.editFoodInteractor = editFoodInteractor;
    }

    public void execute(FoodEntry food, String foodName, FoodNutrition nutrition,
                        double quantity, FoodUnit unit,
                        double grams) {
        editFoodInteractor.execute(new EditFoodInputData(food, foodName, nutrition, quantity, unit, grams));
    }

    public void switchToEdit(FoodEntry food) {

    }
}
