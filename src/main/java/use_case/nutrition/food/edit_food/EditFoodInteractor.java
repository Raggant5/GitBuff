package use_case.nutrition.food.edit_food;

import entity.FoodEntry;
import entity.FoodEntryFactory;
import entity.FoodNutrition;
import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.food.FoodNutritionInput;
import use_case.nutrition.food.FoodValidationErrors;

public class EditFoodInteractor implements EditFoodInputBoundary {

    private final EditFoodOutputBoundary presenter;
    private final FoodEntryFactory foodEntryFactory;

    public EditFoodInteractor(EditFoodOutputBoundary presenter, FoodEntryFactory foodEntryFactory) {
        this.presenter = presenter;
        this.foodEntryFactory = foodEntryFactory;
    }

    @Override
    public void execute(EditFoodInputData inputData) {
        final FoodValidationErrors errors = new FoodValidationErrors();
        if (inputData.getFoodName() == null || inputData.getFoodName().isBlank()) {
            errors.setGeneralError("Food name is required.");
        }

        final FoodNutritionInput nutritionInput = inputData.getNutrition();
        final Double calories = parseNonNegativeDouble(nutritionInput.getCalories());
        final Double protein = parseNonNegativeDouble(nutritionInput.getProtein());
        final Double carbs = parseNonNegativeDouble(nutritionInput.getCarbs());
        final Double fat = parseNonNegativeDouble(nutritionInput.getFat());
        final Double quantity = parseNonNegativeDouble(inputData.getQuantity());
        final Double grams = parseNonNegativeDouble(inputData.getGrams());
        checkFieldErrors(errors, calories, protein, carbs, fat, quantity, grams);

        if (errors.hasErrors()) {
            presenter.prepareFailView(errors);
        }
        else {
            final FoodNutrition nutrition = new FoodNutrition(calories, protein, carbs, fat);
            final FoodEntry food = foodEntryFactory.create(inputData.getFoodName(), nutrition, quantity,
                    inputData.getUnit(), grams);
            final Integer id = inputData.getId();
            if (id != null && id > 0) {
                food.setId(id);
            }

            final FoodNutritionData savedNutrition = new FoodNutritionData(food.getNutrition().getCalories(),
                    food.getNutrition().getProtein(), food.getNutrition().getCarbs(), food.getNutrition().getFat());
            presenter.prepareSuccessView(new EditFoodOutputData(id, food.getFoodName(), savedNutrition,
                    food.getQuantity(), food.getUnit(), food.getGrams()));
        }
    }

    private void checkFieldErrors(FoodValidationErrors errors, Double calories, Double protein, Double carbs,
                                  Double fat, Double quantity, Double grams) {
        if (calories == null) {
            errors.setCaloriesError("Calories must be a non-negative number");
        }
        if (protein == null) {
            errors.setProteinError("Protein must be a non-negative number");
        }
        if (carbs == null) {
            errors.setCarbsError("Carbs must be a non-negative number");
        }
        if (fat == null) {
            errors.setFatError("Fat must be a non-negative number");
        }
        if (quantity == null) {
            errors.setQuantityError("Quantity must be a non-negative number");
        }
        if (grams == null) {
            errors.setGramsError("Grams must be a non-negative number");
        }
    }

    private Double parseNonNegativeDouble(String value) {
        Double result = null;
        try {
            final double parsed = Double.parseDouble(value);
            if (parsed >= 0) {
                result = parsed;
            }
        }
        catch (NumberFormatException exc) {
            result = null;
        }
        return result;
    }
}
