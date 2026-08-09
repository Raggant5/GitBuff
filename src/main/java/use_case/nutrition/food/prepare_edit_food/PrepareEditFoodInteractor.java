package use_case.nutrition.food.prepare_edit_food;

import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.food.FoodNutritionInput;

public class PrepareEditFoodInteractor implements PrepareEditFoodInputBoundary {

    private final PrepareEditFoodOutputBoundary presenter;

    public PrepareEditFoodInteractor(PrepareEditFoodOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(PrepareEditFoodInputData inputData) {
        final FoodNutritionInput input = inputData.getNutrition();
        final FoodNutritionData nutrition = new FoodNutritionData(parseDouble(input.getCalories()),
                parseDouble(input.getProtein()), parseDouble(input.getCarbs()), parseDouble(input.getFat()));
        presenter.prepareSuccessView(new PrepareEditFoodOutputData(inputData.getId(), inputData.getFoodName(),
                nutrition, inputData.getQuantity(), inputData.getUnit(), inputData.getGrams()));
    }

    @Override
    public void switchToAddFoodEditor() {
        presenter.switchToAddFoodEditor();
    }

    private double parseDouble(String value) {
        double result = 0;
        try {
            result = Double.parseDouble(value);
        }
        catch (NumberFormatException exc) {
            result = 0;
        }
        return result;
    }
}
