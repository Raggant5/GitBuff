package use_case.nutrition.food.delete_food;

import entity.FoodEntry;

public class DeleteFoodInteractor implements DeleteFoodInputBoundary {

    private final DeleteFoodOutputBoundary deleteFoodPresenter;
    private final DeleteFoodDataAccessInterface foodDataAccessObject;

    public DeleteFoodInteractor(DeleteFoodOutputBoundary deleteFoodPresenter,
                                DeleteFoodDataAccessInterface foodDataAccessObject) {
        this.deleteFoodPresenter = deleteFoodPresenter;
        this.foodDataAccessObject = foodDataAccessObject;
    }

    @Override
    public void execute(DeleteFoodInputData deleteFoodInputData) {
        final FoodEntry foodEntry = deleteFoodInputData.getFoodEntry();
        deleteFoodPresenter.prepareSuccessView(new DeleteFoodOutputData(foodEntry));
    }
}
