package use_case.nutrition.food.delete_food;

import entity.FoodEntry;

public class DeleteFoodInputData {
    private final FoodEntry foodEntry;

    public DeleteFoodInputData(FoodEntry foodEntry) {
        this.foodEntry = foodEntry;
    }

    public FoodEntry getFoodEntry() {
        return foodEntry;
    }

}
