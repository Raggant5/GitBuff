package use_case.nutrition.meal.edit_meal;

import entity.FoodEntry;
import entity.Meal;
import use_case.nutrition.food.delete_food.DeleteFoodDataAccessInterface;

public class EditMealInteractor implements EditMealInputBoundary {

    private final EditMealOutputBoundary presenter;
    private final EditMealDataAccessInterface dataAccess;
    private final DeleteFoodDataAccessInterface deleteFoodDataAccess;

    public EditMealInteractor(EditMealOutputBoundary presenter, EditMealDataAccessInterface dataAccess,
                              DeleteFoodDataAccessInterface deleteFoodDataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
        this.deleteFoodDataAccess = deleteFoodDataAccess;
    }

    @Override
    public void execute(final EditMealInputData inputData) {
        final Meal meal = inputData.getMeal();

        meal.setName(inputData.getName());
        meal.setFoodEntries(inputData.getFoodEntries());

        for (FoodEntry foodEntry : inputData.getFoodEntriesToDelete()) {
            if (foodEntry.getId() != null) {
                deleteFoodDataAccess.deleteFoodEntry(foodEntry.getId());
            }
        }

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