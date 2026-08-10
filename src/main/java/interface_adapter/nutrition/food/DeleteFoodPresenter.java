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
        final Integer id = deleteFoodOutputData.getId();
        mealEditorViewModel.getState().removeFoodEntryById(id);
        mealEditorViewModel.getState().addFoodEntryToBeDeleted(id);
        mealEditorViewModel.firePropertyChanged();
    }
}
