package use_case.nutrition.food.prepare_edit_food;

public interface PrepareEditFoodInputBoundary {

    void execute(PrepareEditFoodInputData inputData);

    void switchToAddFoodEditor();
}
