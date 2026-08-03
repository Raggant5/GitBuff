package interface_adapter.nutrition.meal;

import java.util.ArrayList;
import java.util.List;

import entity.Meal;
import interface_adapter.MainViewManagerModel;
import use_case.nutrition.meal.add_meal.AddMealOutputBoundary;
import use_case.nutrition.meal.add_meal.AddMealOutputData;

public class AddMealPresenter implements AddMealOutputBoundary {
    private final MealEditorViewModel mealEditorViewModel;
    private final ViewMealsViewModel viewMealsViewModel;
    private final MainViewManagerModel mainViewManagerModel;

    public AddMealPresenter(MealEditorViewModel mealEditorViewModel, ViewMealsViewModel viewMealsViewModel,
                            MainViewManagerModel mainViewManagerModel) {
        this.mealEditorViewModel = mealEditorViewModel;
        this.viewMealsViewModel = viewMealsViewModel;
        this.mainViewManagerModel = mainViewManagerModel;
    }

    @Override
    public void prepareSuccessView(AddMealOutputData outputData) {
        final MealEditorState currentState = mealEditorViewModel.getState();
        currentState.reset();
        mealEditorViewModel.firePropertyChanged();
        final ViewMealsState mealsState = viewMealsViewModel.getState();
        final List<Meal> meals = new ArrayList<>(mealsState.getMeals());
        meals.add(0, outputData.getMeal());        mealsState.setMeals(meals);
        viewMealsViewModel.setState(mealsState);
        viewMealsViewModel.firePropertyChanged();
        mainViewManagerModel.setState("view meals");
        mainViewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        mealEditorViewModel.getState().setErrorMessage(errorMessage);
        mealEditorViewModel.firePropertyChanged();
    }

}
