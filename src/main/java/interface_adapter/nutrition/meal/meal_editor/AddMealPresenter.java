package interface_adapter.nutrition.meal.meal_editor;

import java.util.ArrayList;
import java.util.List;

import entity.Meal;
import interface_adapter.MainViewManagerModel;
import interface_adapter.nutrition.meal.view_meals.ViewMealsState;
import interface_adapter.nutrition.meal.view_meals.ViewMealsViewModel;
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

    public void prepareSuccessView(AddMealOutputData outputData) {
        final MealEditorState currentState = mealEditorViewModel.getState();
        currentState.reset();
        mealEditorViewModel.firePropertyChanged();
        final ViewMealsState mealsState = viewMealsViewModel.getState();
        final List<Meal> meals = new ArrayList<>(mealsState.getMeals());
        meals.add(outputData.getMeal());
        mealsState.setMeals(meals);
        viewMealsViewModel.setState(mealsState);
        viewMealsViewModel.firePropertyChanged();
        mainViewManagerModel.setState("view meals");
        mainViewManagerModel.firePropertyChanged();
    }

    public void prepareFailView(String errorMessage) {

    }

}
