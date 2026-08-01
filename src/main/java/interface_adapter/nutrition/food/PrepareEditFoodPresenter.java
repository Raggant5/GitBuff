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
        final FoodEditorState foodEditorState = foodEditorViewModel.getState();
        foodEditorState.setEditingFood(food);
        foodEditorState.setFoodName(food.getFoodName());
        foodEditorState.setProtein(String.valueOf(food.getNutrition().getProtein()));
        foodEditorState.setCalories(String.valueOf(food.getNutrition().getCalories()));
        foodEditorState.setCarbs(String.valueOf(food.getNutrition().getCarbs()));
        foodEditorState.setFat(String.valueOf(food.getNutrition().getFat()));
        foodEditorState.setQuantity(String.valueOf(food.getQuantity()));
        foodEditorState.setUnit(food.getUnit());
        foodEditorState.setGrams(String.valueOf(food.getGrams()));

        final MealEditorState mealEditorState = mealEditorViewModel.getState();
        mealEditorState.setShowFoodEditor(true);

        mealEditorViewModel.firePropertyChanged();
        foodEditorViewModel.firePropertyChanged();
    }

    @Override
    public void switchToAddFoodEditor() {
        final MealEditorState state = mealEditorViewModel.getState();
        state.setShowFoodEditor(true);
        mealEditorViewModel.firePropertyChanged();
    }
}
