package interface_adapter.nutrition.food;

import entity.FoodEntry;
import use_case.nutrition.food.delete_food.DeleteFoodInputBoundary;
import use_case.nutrition.food.delete_food.DeleteFoodInputData;

public class DeleteFoodController {
    private final DeleteFoodInputBoundary deleteFoodInputInteractor;

    public DeleteFoodController(DeleteFoodInputBoundary deleteFoodInputInteractor) {
        this.deleteFoodInputInteractor = deleteFoodInputInteractor;
    }

    /**
     * Executes the Delete Food Use Case.
     * @param foodEntry food to delete
     */
    public void execute(FoodEntry foodEntry) {
        deleteFoodInputInteractor.execute(new DeleteFoodInputData(foodEntry));
    }

}
