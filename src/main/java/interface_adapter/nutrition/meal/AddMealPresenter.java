package interface_adapter.nutrition.meal;

import java.util.ArrayList;
import java.util.List;

import entity.Meal;
import use_case.nutrition.meal.AddMealOutputBoundary;
import use_case.nutrition.meal.AddMealOutputData;

public class AddMealPresenter implements AddMealOutputBoundary {
    private final AddMealViewModel addMealViewModel;
    private final ViewMealsViewModel viewMealsViewModel;

    public AddMealPresenter(AddMealViewModel addMealViewModel, ViewMealsViewModel viewMealsViewModel) {
        this.addMealViewModel = addMealViewModel;
        this.viewMealsViewModel = viewMealsViewModel;
    }

    public void prepareSuccessView(AddMealOutputData outputData) {
        final MealState currentState = addMealViewModel.getState();
        currentState.reset();
        addMealViewModel.firePropertyChanged();
        final ViewMealsState mealsState = viewMealsViewModel.getState();
        final List<Meal> meals = new ArrayList<>(mealsState.getMeals());
        meals.add(outputData.getMeal());
        mealsState.setMeals(meals);
        viewMealsViewModel.setState(mealsState);
        viewMealsViewModel.firePropertyChanged();
    }

    public void prepareFailView(String errorMessage) {

    }

}
