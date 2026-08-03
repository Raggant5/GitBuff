package interface_adapter.nutrition.food;

import entity.FoodEntry;
import interface_adapter.nutrition.meal.MealEditorState;
import interface_adapter.nutrition.meal.MealEditorViewModel;
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
        final FoodEntry food = outputData.getFood();

        final FoodEditorState state = foodEditorViewModel.getState();
        state.reset();
        state.setEditingFood(food);
        state.setFoodName(food.getFoodName());
        state.setQuantity(String.valueOf(food.getQuantity()));
        state.setUnit(food.getUnit());
        state.setTotalGramsDisplay(String.valueOf(food.getGrams()));
        final double quantity = food.getQuantity();
        state.setServingCalories(food.getNutrition().getCalories() / quantity);
        state.setServingProtein(food.getNutrition().getProtein() / quantity);
        state.setServingCarbs(food.getNutrition().getCarbs() / quantity);
        state.setServingFat(food.getNutrition().getFat() / quantity);
        state.setServingGrams(food.getGrams() / quantity);

        state.setOriginalServingGrams(food.getGrams() / quantity);
        state.recalculateTotals();
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
