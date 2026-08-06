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
        if (inputData.getFoodName() == null || inputData.getFoodName().isBlank()) {
            addFoodPresenter.prepareFailView("Food name is required.");
        }
        else {
            try {
                final FoodNutritionInputData nutritionInput = inputData.getFoodNutrition();

                final FoodNutrition nutrition = new FoodNutrition(
                        parseNonNegativeDouble(nutritionInput.getCalories(), "Calories"),
                        parseNonNegativeDouble(nutritionInput.getProtein(), "Protein"),
                        parseNonNegativeDouble(nutritionInput.getCarbs(), "Carbs"),
                        parseNonNegativeDouble(nutritionInput.getFat(), "Fat"));

                final FoodEntry food = foodEntryFactory.create(
                        inputData.getFoodName(), nutrition, parseNonNegativeDouble(inputData.getQuantity(), "Quantity"),
                        inputData.getUnit(), parseNonNegativeDouble(inputData.getGrams(), "Grams"));

                addFoodPresenter.prepareSuccessView(new AddFoodEntryOutputData(food));
            }
            catch (NumberFormatException exc) {
                addFoodPresenter.prepareFailView("Please ensure all fields are valid.");
            }
            catch (IllegalArgumentException exc) {
                addFoodPresenter.prepareFailView("Please ensure all fields are valid.");
            }
        }
    }

    private double parseNonNegativeDouble(String value, String fieldName) {
        final double parsed = Double.parseDouble(value);
        if (parsed < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative.");
        }
        return parsed;
    }
}
