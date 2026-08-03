package use_case.nutrition.meal.edit_meal;

import entity.FoodEntry;
import entity.Meal;

public class EditMealInteractor implements EditMealInputBoundary {

    private final EditMealOutputBoundary presenter;
    private final EditMealDataAccessInterface dataAccess;

    public EditMealInteractor(
            final EditMealOutputBoundary presenter,
            final EditMealDataAccessInterface dataAccess
    ) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(final EditMealInputData inputData) {
        final Meal meal = inputData.getMeal();

        meal.setName(inputData.getName());
        meal.setFoodEntries(inputData.getFoodEntries());

        dataAccess.editMeal(meal);

        for (FoodEntry foodEntry : inputData.getFoodEntries()) {
            if (foodEntry.getId() == null) {
                foodEntry.setMealId(meal.getId());

                final int foodEntryId =
                        dataAccess.saveFoodEntry(foodEntry);

                foodEntry.setId(foodEntryId);
            }
            else {
                dataAccess.editFoodEntry(foodEntry);
            }
        }

        presenter.prepareSuccessView(
                new EditMealOutputData(meal)
        );
    }
}