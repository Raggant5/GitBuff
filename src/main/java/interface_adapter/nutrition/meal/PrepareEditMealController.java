package interface_adapter.nutrition.meal;

import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealInputBoundary;
import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealInputData;

public class PrepareEditMealController {

    private final PrepareEditMealInputBoundary prepareEditMealInteractor;

    public PrepareEditMealController(PrepareEditMealInputBoundary prepareEditMealInteractor) {
        this.prepareEditMealInteractor = prepareEditMealInteractor;
    }

    /**
     * Executes the Prepare Edit Meal Use Case.
     * @param mealId the id of the meal to edit
     */
    public void execute(int mealId) {
        prepareEditMealInteractor.execute(new PrepareEditMealInputData(mealId));
    }

}
