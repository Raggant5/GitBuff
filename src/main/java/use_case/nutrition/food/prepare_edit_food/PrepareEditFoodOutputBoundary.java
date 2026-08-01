package use_case.nutrition.food.prepare_edit_food;

public interface PrepareEditFoodOutputBoundary {

    /**
     * Prepares the success view for switching to editing food mode.
     * @param outputData the food data to be edited upon
     */
    void prepareSuccessView(PrepareEditFoodOutputData outputData);

    /**
     * Executes the "Switch to Add Food" Use Case.
     */
    void switchToAddFoodEditor();
}
