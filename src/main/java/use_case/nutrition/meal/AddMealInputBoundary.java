package use_case.nutrition.meal;

/**
 * Input Boundary for actions which are related to adding a meal consumed.
 */
public interface AddMealInputBoundary {

    /**
     * Executes the add meal use case.
     * @param addMealInputData the input data
     */
    void execute(AddMealInputData addMealInputData);
}
