package use_case.nutrition.food.edit_food;

import entity.FoodEntry;

public class EditFoodOutputData {

    private final FoodEntry food;

    public EditFoodOutputData(FoodEntry food) {
        this.food = food;
    }

    public FoodEntry getFood() {
        return food;
    }

}
