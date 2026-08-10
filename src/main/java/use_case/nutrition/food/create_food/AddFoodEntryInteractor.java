package use_case.nutrition.food.create_food;

import entity.FoodEntry;
import entity.FoodEntryFactory;
import entity.FoodNutrition;
import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.food.FoodNutritionInput;
import use_case.nutrition.food.FoodValidationErrors;

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
        final FoodValidationErrors errors = new FoodValidationErrors();
        if (inputData.getFoodName() == null || inputData.getFoodName().isBlank()) {
            errors.setGeneralError("Food name is required.");
        }

        final FoodNutritionInput nutritionInput = inputData.getFoodNutrition();
        final Double calories = parseNonNegativeDouble(nutritionInput.getCalories());
        final Double protein = parseNonNegativeDouble(nutritionInput.getProtein());
        final Double carbs = parseNonNegativeDouble(nutritionInput.getCarbs());
        final Double fat = parseNonNegativeDouble(nutritionInput.getFat());
        final Double quantity = parseNonNegativeDouble(inputData.getQuantity());
        final Double grams = parseNonNegativeDouble(inputData.getGrams());
        checkFieldErrors(errors, calories, protein, carbs, fat, quantity, grams);

        if (errors.hasErrors()) {
            addFoodPresenter.prepareFailView(errors);
        }
        else {
            final FoodNutrition nutrition = new FoodNutrition(calories, protein, carbs, fat);
            final FoodEntry food = foodEntryFactory.create(inputData.getFoodName(), nutrition, quantity,
                    inputData.getUnit(), grams);

            final FoodNutritionData savedNutrition = new FoodNutritionData(food.getNutrition().getCalories(),
                    food.getNutrition().getProtein(), food.getNutrition().getCarbs(), food.getNutrition().getFat());
            addFoodPresenter.prepareSuccessView(new AddFoodEntryOutputData(food.getId(), food.getFoodName(),
                    savedNutrition, food.getQuantity(), food.getUnit(), food.getGrams()));
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
