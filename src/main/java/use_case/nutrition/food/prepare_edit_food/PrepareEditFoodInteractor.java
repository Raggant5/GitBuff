package use_case.nutrition.food.prepare_edit_food;

import entity.FoodEntry;

public class PrepareEditFoodInteractor implements PrepareEditFoodInputBoundary {

    private final PrepareEditFoodOutputBoundary presenter;

    public PrepareEditFoodInteractor(PrepareEditFoodOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(PrepareEditFoodInputData inputData) {
        final FoodEntry food = inputData.getFood();
        presenter.prepareSuccessView(new PrepareEditFoodOutputData(food));
    }

    @Override
    public void switchToAddFoodEditor() {
        presenter.switchToAddFoodEditor();
    }
}
