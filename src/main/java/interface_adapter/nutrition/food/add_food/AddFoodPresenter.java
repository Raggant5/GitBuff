package interface_adapter.nutrition.food.add_food;

import java.util.List;

import entity.FoodEntry;
import interface_adapter.nutrition.food.FoodEditorViewModel;
import interface_adapter.nutrition.meal.meal_editor.MealEditorViewModel;
import interface_adapter.nutrition.meal.meal_editor.MealEditorState;
import use_case.nutrition.food.add_food.AddFoodEntryOutputBoundary;
import use_case.nutrition.food.add_food.AddFoodEntryOutputData;

public class AddFoodPresenter implements AddFoodEntryOutputBoundary {
    private final MealEditorViewModel mealEditorViewModel;
    private final FoodEditorViewModel foodEditorViewModel;

    public AddFoodPresenter(MealEditorViewModel mealEditorViewModel, FoodEditorViewModel foodEditorViewModel) {
        this.mealEditorViewModel = mealEditorViewModel;
        this.foodEditorViewModel = foodEditorViewModel;
    }

    public void prepareSuccessView(AddFoodEntryOutputData outputData) {
        final MealEditorState currentState = mealEditorViewModel.getState();
        final List<FoodEntry> foodEntriesForMeal = currentState.getFoodEntriesForMeal();
        foodEntriesForMeal.add(outputData.getFood());
        currentState.setFoodEntriesForMeal(foodEntriesForMeal);
        mealEditorViewModel.firePropertyChanged();

        foodEditorViewModel.getState().reset();
        foodEditorViewModel.firePropertyChanged();
    }

    public void prepareFailView(String errorMessage) {
    }

}
