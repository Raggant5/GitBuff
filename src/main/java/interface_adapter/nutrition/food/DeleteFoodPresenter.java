package interface_adapter.nutrition.food;

import interface_adapter.nutrition.meal.MealEditorViewModel;
import interface_adapter.nutrition.meal.ViewMealsViewModel;
import use_case.nutrition.food.delete_food.DeleteFoodOutputBoundary;
import use_case.nutrition.food.delete_food.DeleteFoodOutputData;

public class DeleteFoodPresenter implements DeleteFoodOutputBoundary {

    private final MealEditorViewModel mealEditorViewModel;
    private final ViewMealsViewModel viewMealsViewModel;

    public DeleteFoodPresenter(MealEditorViewModel mealEditorViewModel, ViewMealsViewModel viewMealsViewModel) {
        this.mealEditorViewModel = mealEditorViewModel;
        this.viewMealsViewModel = viewMealsViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteFoodOutputData deleteFoodOutputData) {
        mealEditorViewModel.getState().removeFoodEntry(deleteFoodOutputData.getFoodEntry());
        mealEditorViewModel.getState().addFoodEntryToBeDeleted(deleteFoodOutputData.getFoodEntry());
        mealEditorViewModel.firePropertyChanged();

        viewMealsViewModel.getState().removeFoodEntry(deleteFoodOutputData.getFoodEntry());
        viewMealsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {

    }
}
