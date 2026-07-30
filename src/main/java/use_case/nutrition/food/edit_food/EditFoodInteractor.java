package use_case.nutrition.food.edit_food;

import entity.FoodEntry;
import entity.FoodNutrition;
import use_case.nutrition.food.FoodNutritionInputData;

public class EditFoodInteractor implements EditFoodInputBoundary {

    private final EditFoodOutputBoundary presenter;
    private final EditFoodDataAccessInterface dataAccess;

    public EditFoodInteractor(EditFoodOutputBoundary presenter, EditFoodDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(EditFoodInputData inputData) {
        try {
            final FoodEntry food = inputData.getFoodEntry();
            final FoodNutritionInputData nutritionInput = inputData.getNutrition();
            final FoodNutrition nutrition = new FoodNutrition(
                    parseDoubleOrZero(nutritionInput.getCalories()),
                    parseDoubleOrZero(nutritionInput.getProtein()),
                    parseDoubleOrZero(nutritionInput.getCarbs()),
                    parseDoubleOrZero(nutritionInput.getFat()));
            food.setFoodName(inputData.getFoodName());
            food.setNutrition(nutrition);
            food.setQuantity(parseDoubleOrZero(inputData.getQuantity()));
            food.setUnit(inputData.getUnit());
            food.setGrams(parseDoubleOrZero(inputData.getGrams()));
            dataAccess.editFoodEntry(food);

            presenter.prepareSuccessView(new EditFoodOutputData(food));
        }
        catch (NumberFormatException e) {
            presenter.prepareFailView("Please enter valid numbers.");
        }
    }

    private double parseDoubleOrZero(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(value);
    }
}
