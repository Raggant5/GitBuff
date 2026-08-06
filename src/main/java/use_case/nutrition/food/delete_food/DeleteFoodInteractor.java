package use_case.nutrition.food.delete_food;

import entity.FoodEntry;

public class DeleteFoodInteractor implements DeleteFoodInputBoundary {

    private final DeleteFoodOutputBoundary deleteFoodPresenter;

    public DeleteFoodInteractor(DeleteFoodOutputBoundary deleteFoodPresenter) {
        this.deleteFoodPresenter = deleteFoodPresenter;
    }

    @Override
    public void execute(DeleteFoodInputData deleteFoodInputData) {
        final FoodEntry foodEntry = deleteFoodInputData.getFoodEntry();
        deleteFoodPresenter.prepareSuccessView(new DeleteFoodOutputData(foodEntry));
    }
}
