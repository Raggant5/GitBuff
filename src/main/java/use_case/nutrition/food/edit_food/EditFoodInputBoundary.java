package use_case.nutrition.food.edit_food;

public interface EditFoodInputBoundary {

    /**
     * Executes the Edit Food use case.
     * @param inputData the data needed to edit a food entry
     */
    void execute(EditFoodInputData inputData);

}
