package use_case.nutrition.food.prepare_edit_food;

import entity.FoodEntry;

public class PrepareEditFoodInputData {

    private final FoodEntry food;

    public PrepareEditFoodInputData(FoodEntry food) {
        this.food = food;
    }

    public FoodEntry getFood() {
        return food;
    }
}
