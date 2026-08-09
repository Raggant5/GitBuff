package use_case.nutrition.food.delete_food;

public class DeleteFoodInteractor implements DeleteFoodInputBoundary {

    private final DeleteFoodOutputBoundary deleteFoodPresenter;

    public DeleteFoodInteractor(DeleteFoodOutputBoundary deleteFoodPresenter) {
        this.deleteFoodPresenter = deleteFoodPresenter;
    }

    @Override
    public void execute(DeleteFoodInputData deleteFoodInputData) {
        deleteFoodPresenter.prepareSuccessView(new DeleteFoodOutputData(deleteFoodInputData.getId()));
    }
}
