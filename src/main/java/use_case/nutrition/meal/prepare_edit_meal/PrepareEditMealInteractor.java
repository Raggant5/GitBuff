package use_case.nutrition.meal.prepare_edit_meal;

import entity.Meal;

public class PrepareEditMealInteractor implements PrepareEditMealInputBoundary {

    private final PrepareEditMealOutputBoundary presenter;

    public PrepareEditMealInteractor(PrepareEditMealOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(PrepareEditMealInputData inputData) {
        final Meal meal = inputData.getMeal();
        presenter.prepareSuccessView(new PrepareEditMealOutputData(meal));
    }
}
