package use_case.nutrition.food.delete_food;

public class DeleteFoodOutputData {

    private final int foodId;

    public DeleteFoodOutputData(int foodId) {
        this.foodId = foodId;
    }

    public int getFoodId() {
        return foodId;
    }
}
