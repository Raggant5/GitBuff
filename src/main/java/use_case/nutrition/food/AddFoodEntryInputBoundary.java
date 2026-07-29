package use_case.nutrition.food;

/**
 * Input Boundary for actions which are related to adding food within a meal consumed.
 */
public interface AddFoodEntryInputBoundary {

    /**
     * Executes the add food use case.
     * @param addFoodEntryInputData the input data
     */
    void execute(AddFoodEntryInputData addFoodEntryInputData);
}
