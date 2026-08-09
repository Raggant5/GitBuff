package interface_adapter.nutrition.food;

import use_case.nutrition.food.FoodNutritionInput;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodInputBoundary;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodInputData;

public class PrepareEditFoodController {

    private final PrepareEditFoodInputBoundary prepareEditFoodInteractor;

    public PrepareEditFoodController(PrepareEditFoodInputBoundary prepareEditFoodInteractor) {
        this.prepareEditFoodInteractor = prepareEditFoodInteractor;
    }

    /**
     * Executes the Prepare Edit Food Use Case.
     * @param food the food row selected for editing
     */
    public void execute(FoodEntryDisplayData food) {
        final FoodNutritionInput nutrition = new FoodNutritionInput(food.getNutrition().getCalories(),
                food.getNutrition().getProtein(), food.getNutrition().getCarbs(), food.getNutrition().getFat());
        prepareEditFoodInteractor.execute(new PrepareEditFoodInputData(food.getId(), food.getFoodName(),
                nutrition, food.getQuantity(), food.getUnit(), food.getGrams()));
    }

    /**
     * Executes the "Switch To Add Food" Use Case.
     */
    public void switchToAddFoodEditor() {
        prepareEditFoodInteractor.switchToAddFoodEditor();
    }
}
