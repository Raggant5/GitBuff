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
        if (inputData.getFoodName() == null || inputData.getFoodName().isBlank()) {
            presenter.prepareFailView("Food name is required.");
        }
        else {
            try {
                final FoodEntry food = inputData.getFoodEntry();
                final FoodNutritionInputData nutritionInput = inputData.getNutrition();
                final FoodNutrition nutrition = new FoodNutrition(
                        parseNonNegativeDouble(nutritionInput.getCalories(), "Calories"),
                        parseNonNegativeDouble(nutritionInput.getProtein(), "Protein"),
                        parseNonNegativeDouble(nutritionInput.getCarbs(), "Carbs"),
                        parseNonNegativeDouble(nutritionInput.getFat(), "Fat"));
                food.setFoodName(inputData.getFoodName());
                food.setNutrition(nutrition);
                food.setQuantity(parseNonNegativeDouble(inputData.getQuantity(), "Quantity"));
                food.setUnit(inputData.getUnit());
                food.setGrams(parseNonNegativeDouble(inputData.getGrams(), "Grams"));
                if (food.getId() != null) {
                    dataAccess.editFoodEntry(food);
                }

                presenter.prepareSuccessView(new EditFoodOutputData(food));
            }
            catch (NumberFormatException exc) {
                presenter.prepareFailView("Please ensure all fields are valid.");
            }
            catch (IllegalArgumentException exc) {
                presenter.prepareFailView("Please ensure all fields are valid.");
            }
            catch (RuntimeException exc) {
                presenter.prepareFailView("Unable to save food entry. Please try again.");
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
