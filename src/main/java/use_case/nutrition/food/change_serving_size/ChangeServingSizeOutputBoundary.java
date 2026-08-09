package use_case.nutrition.food.change_serving_size;

public interface ChangeServingSizeOutputBoundary {

    /**
     * The output boundary for the Change Serving Size Use Case.
     * @param outputData the recalculated serving data
     */
    void prepareSuccessView(ChangeServingSizeOutputData outputData);

}
