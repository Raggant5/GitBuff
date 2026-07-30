package use_case.nutrition.food.create_food;

import entity.FoodEntry;

/**
 * Output Data for the Add Food Entry Use Case.
 */
public class AddFoodEntryOutputData {

    private final FoodEntry food;

    public AddFoodEntryOutputData(FoodEntry food) {
        this.food = food;
    }

    public FoodEntry getFood() {
        return food;
    }

}
