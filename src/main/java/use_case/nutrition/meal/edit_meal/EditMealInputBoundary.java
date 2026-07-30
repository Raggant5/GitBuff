package use_case.nutrition.meal.edit_meal;

public interface EditMealInputBoundary {

    /**
     * Executes the Edit Meal use case.
     *
     * @param inputData the input data required to edit a meal
     */
    void execute(EditMealInputData inputData);

}