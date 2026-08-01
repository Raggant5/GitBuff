package interface_adapter.nutrition.food;

import java.util.List;
import java.util.Objects;

import entity.FoodEntry;
import interface_adapter.nutrition.meal.MealEditorState;
import interface_adapter.nutrition.meal.MealEditorViewModel;
import use_case.nutrition.food.edit_food.EditFoodOutputBoundary;
import use_case.nutrition.food.edit_food.EditFoodOutputData;

public class EditFoodPresenter implements EditFoodOutputBoundary {

    private final MealEditorViewModel mealEditorViewModel;
    private final FoodEditorViewModel foodEditorViewModel;

    public EditFoodPresenter(MealEditorViewModel mealEditorViewModel, FoodEditorViewModel foodEditorViewModel) {
        this.mealEditorViewModel = mealEditorViewModel;
        this.foodEditorViewModel = foodEditorViewModel;
    }

    @Override
    public void prepareSuccessView(EditFoodOutputData outputData) {

        final MealEditorState state = mealEditorViewModel.getState();
        final List<FoodEntry> foods = state.getFoodEntriesForMeal();
        for (int i = 0; i < foods.size(); i++) {
            if (Objects.equals(foods.get(i).getId(), outputData.getFood().getId())) {
                foods.set(i, outputData.getFood());
                break;
            }
        }

        state.setFoodEntriesForMeal(foods);
        state.setShowFoodEditor(false);
        mealEditorViewModel.firePropertyChanged();
        foodEditorViewModel.getState().reset();
        foodEditorViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        foodEditorViewModel.getState().setError(errorMessage);
        foodEditorViewModel.firePropertyChanged();

    }
}
