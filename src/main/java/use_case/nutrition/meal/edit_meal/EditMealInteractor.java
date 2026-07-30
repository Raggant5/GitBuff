package use_case.nutrition.meal.edit_meal;

import entity.Meal;

public class EditMealInteractor implements EditMealInputBoundary {

    private final EditMealOutputBoundary presenter;
    private final EditMealDataAccessInterface dataAccess;

    public EditMealInteractor(EditMealOutputBoundary presenter,  EditMealDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(EditMealInputData inputData) {
        final Meal meal = inputData.getMeal();
        meal.setName(inputData.getName());
        meal.setFoodEntries(inputData.getFoodEntries());

        dataAccess.editMeal(meal);

        presenter.prepareSuccessView(new EditMealOutputData(meal));
    }
}