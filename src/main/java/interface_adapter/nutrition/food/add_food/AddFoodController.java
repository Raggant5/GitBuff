package interface_adapter.nutrition.food.add_food;

import entity.FoodNutrition;
import entity.FoodUnit;
import use_case.nutrition.food.add_food.AddFoodEntryInputBoundary;
import use_case.nutrition.food.add_food.AddFoodEntryInputData;

public class AddFoodController {

    private final AddFoodEntryInputBoundary addFoodInteractor;

    public AddFoodController(AddFoodEntryInputBoundary addFoodInteractor) {
        this.addFoodInteractor = addFoodInteractor;
    }

    /**
     * Executes the Add Food Use Case.
     * @param foodName the name given for the food
     * @param nutrition the nutritional value consumed
     * @param quantity the total amount in the given units
     * @param unit the units of which the quantity was consumed
     * @param grams total amount in grams
     */
    public void execute(String foodName, FoodNutrition nutrition, double quantity, FoodUnit unit, double grams) {
        addFoodInteractor.execute(new AddFoodEntryInputData(foodName, nutrition,
                quantity, unit, grams));
    }

}
