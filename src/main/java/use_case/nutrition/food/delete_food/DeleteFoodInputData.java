package use_case.nutrition.food.delete_food;

public class DeleteFoodInputData {
    private final int foodId;

    public DeleteFoodInputData(int foodId) {
        this.foodId = foodId;
    }

    public int getFoodId() {
        return foodId;
    }

}
