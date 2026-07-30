package use_case.nutrition.food.prepare_edit_food;

public interface PrepareEditFoodOutputBoundary {

    void prepareSuccessView(
            PrepareEditFoodOutputData outputData
    );
    
    void switchToAddFoodEditor();
}
