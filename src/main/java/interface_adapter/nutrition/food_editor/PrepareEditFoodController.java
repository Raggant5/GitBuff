package interface_adapter.nutrition.food_editor;

import entity.FoodEntry;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodInputBoundary;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodInputData;

public class PrepareEditFoodController {

    private final PrepareEditFoodInputBoundary prepareEditFoodInteractor;

    public PrepareEditFoodController(PrepareEditFoodInputBoundary prepareEditFoodInteractor) {
        this.prepareEditFoodInteractor = prepareEditFoodInteractor;
    }

    /**
     * Executes the Prepare Edit Food Use Case.
     * @param food the food to be edited
     */
    public void execute(FoodEntry food) {
        prepareEditFoodInteractor.execute(new PrepareEditFoodInputData(food));
    }

    /**
     * Executes the "Switch To Add Food" Use Case.
     */
    public void switchToAddFoodEditor() {
        prepareEditFoodInteractor.switchToAddFoodEditor();
    }
}
