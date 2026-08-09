package interface_adapter.nutrition.food;

import interface_adapter.nutrition.meal.MealEditorState;
import interface_adapter.nutrition.meal.MealEditorViewModel;
import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodOutputBoundary;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodOutputData;

public class PrepareEditFoodPresenter implements PrepareEditFoodOutputBoundary {
    private final MealEditorViewModel mealEditorViewModel;
    private final FoodEditorViewModel foodEditorViewModel;

    public PrepareEditFoodPresenter(FoodEditorViewModel foodEditorViewModel, MealEditorViewModel mealEditorViewModel) {
        this.foodEditorViewModel = foodEditorViewModel;
        this.mealEditorViewModel = mealEditorViewModel;
    }

    @Override
    public void prepareSuccessView(PrepareEditFoodOutputData outputData) {
        final FoodNutritionData nutrition = outputData.getNutrition();
        final double calories = nutrition.getCalories();
        final double protein = nutrition.getProtein();
        final double carbs = nutrition.getCarbs();
        final double fat = nutrition.getFat();

        final FoodEditorState state = foodEditorViewModel.getState();
        state.reset();
        state.setEditingFoodEntryId(outputData.getId());
        state.setFoodName(outputData.getFoodName());
        final FoodServingDetails servingDetails = state.getServingDetails();
        servingDetails.setQuantity(String.valueOf(outputData.getQuantity()));
        servingDetails.setUnit(outputData.getUnit());
        servingDetails.setTotalGramsDisplay(String.valueOf(outputData.getGrams()));
        final double quantity = outputData.getQuantity();
        if (quantity != 0) {
            servingDetails.setServingCalories(calories / quantity);
            servingDetails.setServingProtein(protein / quantity);
            servingDetails.setServingCarbs(carbs / quantity);
            servingDetails.setServingFat(fat / quantity);
            servingDetails.setServingGrams(outputData.getGrams() / quantity);

            servingDetails.setOriginalServingGrams(outputData.getGrams() / quantity);
        }
        servingDetails.recalculateTotals();
        final MealEditorState mealState = mealEditorViewModel.getState();
        mealState.setShowFoodEditor(true);
        foodEditorViewModel.firePropertyChanged();
        mealEditorViewModel.firePropertyChanged();
    }

    @Override
    public void switchToAddFoodEditor() {
        final MealEditorState state = mealEditorViewModel.getState();
        state.setShowFoodEditor(true);
        mealEditorViewModel.firePropertyChanged();
    }
}
