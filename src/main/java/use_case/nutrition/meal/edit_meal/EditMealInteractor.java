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
        if (inputData.getName() == null || inputData.getName().isBlank()) {
            presenter.prepareFailView("Meal name is required.");
        }
        else {
            try {
                final Meal meal = inputData.getMeal();

                meal.setName(inputData.getName());
                meal.setFoodEntries(inputData.getFoodEntries());

                for (FoodEntry foodEntry : inputData.getFoodEntriesToDelete()) {
                    if (foodEntry.getId() != null) {
                        deleteFoodDataAccess.deleteFoodEntry(foodEntry.getId());
                    }
                }

                dataAccess.editMeal(meal);

                presenter.prepareSuccessView(new EditMealOutputData(meal));
            }
            catch (RuntimeException exc) {
                presenter.prepareFailView("Unable to save meal. Please try again.");
            }
        }
    }
}
