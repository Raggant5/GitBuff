package interface_adapter.nutrition.food;

import use_case.nutrition.food.FoodNutritionInput;
import use_case.nutrition.food.create_food.AddFoodEntryInputBoundary;
import use_case.nutrition.food.create_food.AddFoodEntryInputData;

public class AddFoodController {

    private final AddFoodEntryInputBoundary addFoodInteractor;

    public AddFoodController(AddFoodEntryInputBoundary addFoodInteractor) {
        this.addFoodInteractor = addFoodInteractor;
    }

    /**
     * Executes the Add Food Use Case.
     * @param foodName the name given for the food
     * @param nutritionDisplayData the nutritional value consumed
     * @param quantity the total amount in the given units
     * @param unit the units of which the quantity was consumed
     * @param grams total amount in grams
     */
    public void execute(String foodName, FoodNutritionDisplayData nutritionDisplayData,
                        String quantity, FoodUnitOption unit, String grams) {
        final FoodNutritionInput nutrition = new FoodNutritionInput(nutritionDisplayData.getCalories(),
                nutritionDisplayData.getProtein(), nutritionDisplayData.getCarbs(), nutritionDisplayData.getFat());
        addFoodInteractor.execute(new AddFoodEntryInputData(foodName, nutrition,
                quantity, FoodEnumMapper.toEntity(unit), grams));
    }

}
