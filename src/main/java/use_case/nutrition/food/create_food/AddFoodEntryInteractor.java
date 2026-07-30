package use_case.nutrition.food.create_food;

import entity.FoodEntry;
import entity.FoodEntryFactory;
import entity.FoodNutrition;
import use_case.nutrition.food.FoodNutritionInputData;

public class AddFoodEntryInteractor implements AddFoodEntryInputBoundary {

    private final AddFoodEntryOutputBoundary addFoodPresenter;
    private final FoodEntryFactory foodEntryFactory;

    public AddFoodEntryInteractor(AddFoodEntryOutputBoundary addFoodPresenter,
                                  FoodEntryFactory foodEntryFactory) {
        this.addFoodPresenter = addFoodPresenter;
        this.foodEntryFactory = foodEntryFactory;
    }
    @Override
    public void execute(AddFoodEntryInputData inputData) {
        try {
            FoodNutritionInputData nutritionInput = inputData.getFoodNutrition();

            FoodNutrition nutrition = new FoodNutrition(
                    parseDoubleOrZero(nutritionInput.getCalories()),
                    parseDoubleOrZero(nutritionInput.getProtein()),
                    parseDoubleOrZero(nutritionInput.getCarbs()),
                    parseDoubleOrZero(nutritionInput.getFat())
            );

            FoodEntry food = foodEntryFactory.create(
                    inputData.getFoodName(),
                    nutrition,
                    parseDoubleOrZero(inputData.getQuantity()),
                    inputData.getUnit(),
                    parseDoubleOrZero(inputData.getGrams())
            );

            addFoodPresenter.prepareSuccessView(
                    new AddFoodEntryOutputData(food)
            );
        }
        catch (NumberFormatException e) {
            addFoodPresenter.prepareFailView("Please enter valid numbers.");
        }
    }

    private double parseDoubleOrZero(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(value);
    }
}
