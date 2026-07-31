package use_case.nutrition.food.delete_food;

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
        foodDataAccessObject.deleteFoodEntry(deleteFoodInputData.getFoodId());
        deleteFoodPresenter.prepareSuccessView(new DeleteFoodOutputData(deleteFoodInputData.getFoodId()));
    }
}
