package use_case.nutrition.food.prepare_edit_food;

public interface PrepareEditFoodInputBoundary {

    /**
     * Executes the Prepare Edit Food use case.
     * @param inputData the data to be edited upon
     */
    void execute(PrepareEditFoodInputData inputData);
}
