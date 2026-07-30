package interface_adapter.nutrition.food_editor;

import entity.FoodEntry;
import entity.FoodUnit;
import use_case.nutrition.food.FoodNutritionInputData;
import use_case.nutrition.food.edit_food.EditFoodInputBoundary;
import use_case.nutrition.food.edit_food.EditFoodInputData;

public class EditFoodController {

    private final EditFoodInputBoundary editFoodInteractor;

    public EditFoodController(EditFoodInputBoundary editFoodInteractor) {
        this.editFoodInteractor = editFoodInteractor;
    }

    /**
     * Executes the Edit Food Use Case.
     * @param food the food that is being edited
     * @param foodName the new food name
     * @param nutrition the new nutritional value of the food consumed
     * @param quantity the new quantity of food in the units
     * @param unit the new units which the quantity of food is measured in
     * @param grams the new amount in grams
     */
    public void execute(FoodEntry food, String foodName, FoodNutritionInputData nutrition, String quantity,
                        FoodUnit unit,
                        String grams) {
        editFoodInteractor.execute(new EditFoodInputData(food, foodName, nutrition, quantity, unit, grams));
    }

    /**
     * Executes the "Switch To Edit Food" Use Case.
     * @param food the food to edit
     */
    public void switchToEdit(FoodEntry food) {

    }
}
