package use_case.nutrition.food.change_serving_size;

public interface ChangeServingSizeInputBoundary {

    /**
     * Input Boundary for actions which are related to recalculating serving amounts.
     * @param inputData the current serving data and the newly selected unit
     */
    void execute(ChangeServingSizeInputData inputData);

}
