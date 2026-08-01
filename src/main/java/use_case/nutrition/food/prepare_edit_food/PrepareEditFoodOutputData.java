package use_case.nutrition.food.prepare_edit_food;

import entity.FoodEntry;

public class PrepareEditFoodOutputData {

    private final FoodEntry food;

    public PrepareEditFoodOutputData(FoodEntry food) {
        this.food = food;
    }

    public FoodEntry getFood() {
        return food;
    }
}
