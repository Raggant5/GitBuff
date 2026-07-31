package interface_adapter.nutrition.meal;

import java.util.List;

import entity.FoodEntry;
import entity.Meal;
import use_case.nutrition.meal.edit_meal.EditMealInputBoundary;
import use_case.nutrition.meal.edit_meal.EditMealInputData;

public class EditMealController {

    private final EditMealInputBoundary editMealInteractor;

    public EditMealController(EditMealInputBoundary editMealInteractor) {
        this.editMealInteractor = editMealInteractor;
    }

    /**
     * Executes the Edit Meal Use Case.
     * @param meal the meal entity being edited
     * @param name the updated meal name
     * @param foodEntries the updated list of food entries
     */
    public void execute(Meal meal, String name, List<FoodEntry> foodEntries) {
        editMealInteractor.execute(new EditMealInputData(meal, name, foodEntries));
    }

}
