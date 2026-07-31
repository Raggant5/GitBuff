package use_case.nutrition.meal.delete_meal;

public class DeleteMealInteractor implements DeleteMealInputBoundary {

    private final DeleteMealOutputBoundary deleteMealPresenter;
    private final DeleteMealDataAccessInterface mealDataAccessObject;

    public DeleteMealInteractor(DeleteMealOutputBoundary deleteMealPresenter,
                                DeleteMealDataAccessInterface mealDataAccessObject) {
        this.deleteMealPresenter = deleteMealPresenter;
        this.mealDataAccessObject = mealDataAccessObject;
    }

    @Override
    public void execute(DeleteMealInputData deleteMealInputData) {
        mealDataAccessObject.deleteMeal(deleteMealInputData.getMealId());
        deleteMealPresenter.prepareSuccessView(new DeleteMealOutputData(deleteMealInputData.getMealId()));
    }
}
