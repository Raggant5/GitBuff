package interface_adapter.nutrition.food;

import use_case.nutrition.food.delete_food.DeleteFoodInputBoundary;
import use_case.nutrition.food.delete_food.DeleteFoodInputData;

public class DeleteFoodController {
    private final DeleteFoodInputBoundary deleteFoodInputInteractor;

    public DeleteFoodController(DeleteFoodInputBoundary deleteFoodInputInteractor) {
        this.deleteFoodInputInteractor = deleteFoodInputInteractor;
    }

    /**
     * Executes the Delete Food Use Case.
     * @param id id of the food entry to delete
     */
    public void execute(Integer id) {
        deleteFoodInputInteractor.execute(new DeleteFoodInputData(id));
    }

}
