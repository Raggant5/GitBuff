package interface_adapter.nutrition.meal;

import entity.Meal;
import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealInputBoundary;
import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealInputData;

public class PrepareEditMealController {

    private final PrepareEditMealInputBoundary prepareEditMealInteractor;

    public PrepareEditMealController(PrepareEditMealInputBoundary prepareEditMealInteractor) {
        this.prepareEditMealInteractor = prepareEditMealInteractor;
    }

    /**
     * Executes the Prepare Edit Meal Use Case.
     * @param meal the meal to be edited
     */
    public void execute(Meal meal) {
        prepareEditMealInteractor.execute(new PrepareEditMealInputData(meal));
    }

}
