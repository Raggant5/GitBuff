package use_case.nutrition.food.delete_food;

import entity.FoodEntry;

public class DeleteFoodOutputData {

    private final FoodEntry foodEntry;

    public DeleteFoodOutputData(FoodEntry foodEntry) {
        this.foodEntry = foodEntry;
    }

    public FoodEntry getFoodEntry() {
        return foodEntry;
    }
}
