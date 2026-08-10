package interface_adapter.nutrition.food;

import use_case.nutrition.food.FoodNutritionInput;
import use_case.nutrition.food.edit_food.EditFoodInputBoundary;
import use_case.nutrition.food.edit_food.EditFoodInputData;

public class EditFoodController {

    private final EditFoodInputBoundary editFoodInteractor;

    public EditFoodController(EditFoodInputBoundary editFoodInteractor) {
        this.editFoodInteractor = editFoodInteractor;
    }

    /**
     * Executes the Edit Food Use Case.
     * @param id the id of the food entry being edited (temporary local id if not yet saved)
     * @param foodName the new food name
     * @param nutritionDisplayData the new nutritional value of the food consumed
     * @param quantity the new quantity of food in the units
     * @param unit the new units which the quantity of food is measured in
     * @param grams the new amount in grams
     */
    public void execute(Integer id, String foodName, FoodNutritionDisplayData nutritionDisplayData, String quantity,
                        FoodUnitOption unit, String grams) {
        final FoodNutritionInput nutrition = new FoodNutritionInput(nutritionDisplayData.getCalories(),
                nutritionDisplayData.getProtein(), nutritionDisplayData.getCarbs(), nutritionDisplayData.getFat());
        editFoodInteractor.execute(new EditFoodInputData(id, foodName, nutrition, quantity,
                FoodEnumMapper.toEntity(unit), grams));
    }
}
