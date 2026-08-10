package use_case.nutrition.meal.delete_meal;

import use_case.DataAccessException;

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
        try {
            mealDataAccessObject.deleteMeal(deleteMealInputData.getMealId());
            deleteMealPresenter.prepareSuccessView(new DeleteMealOutputData(deleteMealInputData.getMealId()));
        }
        catch (DataAccessException exc) {
            deleteMealPresenter.prepareFailView("Unable to delete meal. Please try again.");
        }
    }
}
