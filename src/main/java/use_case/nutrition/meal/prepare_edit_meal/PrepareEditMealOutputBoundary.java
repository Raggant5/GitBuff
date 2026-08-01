package use_case.nutrition.meal.prepare_edit_meal;

public interface PrepareEditMealOutputBoundary {

    /**
     * Prepares the success view for switching to editing meal mode.
     * @param outputData the meal to be edited upon
     */
    void prepareSuccessView(PrepareEditMealOutputData outputData);

    /**
     * Executes the "Switch to Add Meal" Use Case.
     */
    void switchToAddMealEditor();
}
