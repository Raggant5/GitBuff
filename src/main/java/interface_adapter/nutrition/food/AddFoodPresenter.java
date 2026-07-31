package interface_adapter.nutrition.food;

import interface_adapter.nutrition.meal.MealEditorState;
import interface_adapter.nutrition.meal.MealEditorViewModel;
import use_case.nutrition.food.create_food.AddFoodEntryOutputBoundary;
import use_case.nutrition.food.create_food.AddFoodEntryOutputData;

public class AddFoodPresenter implements AddFoodEntryOutputBoundary {
    private final MealEditorViewModel mealEditorViewModel;
    private final FoodEditorViewModel foodEditorViewModel;

    public AddFoodPresenter(MealEditorViewModel mealEditorViewModel, FoodEditorViewModel foodEditorViewModel) {
        this.mealEditorViewModel = mealEditorViewModel;
        this.foodEditorViewModel = foodEditorViewModel;
    }

    @Override
    public void prepareSuccessView(AddFoodEntryOutputData outputData) {
        final MealEditorState currentState = mealEditorViewModel.getState();
        currentState.addFoodEntry(outputData.getFood());
        currentState.setShowFoodEditor(false);
        mealEditorViewModel.firePropertyChanged();
        foodEditorViewModel.getState().reset();
        foodEditorViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final MealEditorState currentState = mealEditorViewModel.getState();
        currentState.setErrorMessage(errorMessage);
        mealEditorViewModel.firePropertyChanged();
    }

}
