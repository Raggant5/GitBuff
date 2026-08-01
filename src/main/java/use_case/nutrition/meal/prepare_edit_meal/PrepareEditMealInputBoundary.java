package use_case.nutrition.meal.prepare_edit_meal;

public interface PrepareEditMealInputBoundary {

    /**
     * Executes the Prepare Edit Meal use case.
     * @param inputData the meal to be edited upon
     */
    void execute(PrepareEditMealInputData inputData);

    /**
     * Executes the "Switch to Add Meal" Use Case.
     */
    void switchToAddMealEditor();
}
