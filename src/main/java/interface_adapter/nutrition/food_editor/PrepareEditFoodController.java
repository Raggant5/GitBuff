package interface_adapter.nutrition.food_editor;

import entity.FoodEntry;
import interface_adapter.nutrition.meal_editor.MealEditorState;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodInputBoundary;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodInputData;

public class PrepareEditFoodController {

    private final PrepareEditFoodInputBoundary prepareEditFoodInteractor;

    public PrepareEditFoodController(PrepareEditFoodInputBoundary prepareEditFoodInteractor) {
        this.prepareEditFoodInteractor = prepareEditFoodInteractor;
    }

    public void execute(FoodEntry food) {

        prepareEditFoodInteractor.execute(
                new PrepareEditFoodInputData(food)
        );
    }

    /**
     * Switches back to add food view.
     */
    public void switchToAddFoodEditor() {
        prepareEditFoodInteractor.switchToAddFoodEditor();
    }
}
