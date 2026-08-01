package interface_adapter.nutrition.meal;

import java.util.ArrayList;

import entity.Meal;
import interface_adapter.MainViewManagerModel;
import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealOutputBoundary;
import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealOutputData;

public class PrepareEditMealPresenter implements PrepareEditMealOutputBoundary {
    private final MealEditorViewModel mealEditorViewModel;
    private final MainViewManagerModel mainViewManagerModel;

    public PrepareEditMealPresenter(MealEditorViewModel mealEditorViewModel, MainViewManagerModel mainViewManagerModel) {
        this.mealEditorViewModel = mealEditorViewModel;
        this.mainViewManagerModel = mainViewManagerModel;
    }

    @Override
    public void prepareSuccessView(PrepareEditMealOutputData outputData) {
        final Meal meal = outputData.getMeal();
        final MealEditorState mealEditorState = mealEditorViewModel.getState();
        mealEditorState.setEditingMeal(meal);
        mealEditorState.setDate(meal.getDate());
        mealEditorState.setName(meal.getName());
        mealEditorState.setErrorMessage("");
        mealEditorState.setFoodEntriesForMeal(new ArrayList<>(meal.getFoodEntries()));

        mainViewManagerModel.setState("meal editor");
        mainViewManagerModel.firePropertyChanged();
        mealEditorViewModel.firePropertyChanged();
    }

    @Override
    public void switchToAddMealEditor() {
        mainViewManagerModel.setState("view meals");
        mainViewManagerModel.firePropertyChanged();
    }
}
