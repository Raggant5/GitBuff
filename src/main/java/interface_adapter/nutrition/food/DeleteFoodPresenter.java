package interface_adapter.nutrition.food;

import interface_adapter.nutrition.meal.MealEditorViewModel;
import use_case.nutrition.food.delete_food.DeleteFoodOutputBoundary;
import use_case.nutrition.food.delete_food.DeleteFoodOutputData;

public class DeleteFoodPresenter implements DeleteFoodOutputBoundary {

    private final MealEditorViewModel mealEditorViewModel;

    public DeleteFoodPresenter(MealEditorViewModel mealEditorViewModel) {
        this.mealEditorViewModel = mealEditorViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteFoodOutputData deleteFoodOutputData) {
        mealEditorViewModel.getState().removeFoodEntry(deleteFoodOutputData.getFoodEntry());
        mealEditorViewModel.getState().addFoodEntryToBeDeleted(deleteFoodOutputData.getFoodEntry());
        mealEditorViewModel.firePropertyChanged();
    }
}
