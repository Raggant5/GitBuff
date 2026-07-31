package interface_adapter.nutrition.meal_editor;

import java.util.List;
import java.util.Objects;

import entity.Meal;
import interface_adapter.MainViewManagerModel;
import interface_adapter.nutrition.meals.ViewMealsState;
import interface_adapter.nutrition.meals.ViewMealsViewModel;
import use_case.nutrition.meal.edit_meal.EditMealOutputBoundary;
import use_case.nutrition.meal.edit_meal.EditMealOutputData;

public class EditMealPresenter implements EditMealOutputBoundary {

    private final ViewMealsViewModel viewMealsViewModel;
    private final MealEditorViewModel mealEditorViewModel;
    private final MainViewManagerModel mainViewManagerModel;

    public EditMealPresenter(ViewMealsViewModel viewMealsViewModel, MealEditorViewModel mealEditorViewModel,
                             MainViewManagerModel mainViewManagerModel) {
        this.viewMealsViewModel = viewMealsViewModel;
        this.mealEditorViewModel = mealEditorViewModel;
        this.mainViewManagerModel = mainViewManagerModel;
    }

    @Override
    public void prepareSuccessView(EditMealOutputData outputData) {
        final ViewMealsState viewMealsState = viewMealsViewModel.getState();
        final List<Meal> meals = viewMealsState.getMeals();
        for (int i = 0; i < meals.size(); i++) {
            if (Objects.equals(meals.get(i).getId(), outputData.getMeal().getId())) {
                meals.set(i, outputData.getMeal());
                break;
            }
        }
        viewMealsState.setMeals(meals);
        viewMealsViewModel.firePropertyChanged();
        mealEditorViewModel.getState().reset();
        mealEditorViewModel.firePropertyChanged();

        mainViewManagerModel.setState("view meals");
        mainViewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {

        final MealEditorState state = mealEditorViewModel.getState();
        state.setErrorMessage(errorMessage);
        mealEditorViewModel.firePropertyChanged();

    }
}
