package interface_adapter.nutrition.food;

import java.util.List;

import entity.FoodEntry;
import interface_adapter.nutrition.meal.AddMealViewModel;
import interface_adapter.nutrition.meal.MealState;
import use_case.nutrition.food.AddFoodEntryOutputBoundary;
import use_case.nutrition.food.AddFoodEntryOutputData;

public class AddFoodPresenter implements AddFoodEntryOutputBoundary {
    private final AddMealViewModel addMealViewModel;
    private final FoodViewModel foodViewModel;

    public AddFoodPresenter(AddMealViewModel addMealViewModel, FoodViewModel foodViewModel) {
        this.addMealViewModel = addMealViewModel;
        this.foodViewModel = foodViewModel;
    }

    public void prepareSuccessView(AddFoodEntryOutputData outputData) {
        final MealState currentState = addMealViewModel.getState();
        final List<FoodEntry> foodEntriesForMeal = currentState.getFoodEntriesForMeal();
        foodEntriesForMeal.add(outputData.getFood());
        currentState.setFoodEntriesForMeal(foodEntriesForMeal);
        addMealViewModel.firePropertyChanged();

        foodViewModel.getState().reset();
        foodViewModel.firePropertyChanged();
    }

    public void prepareFailView(String errorMessage) {
    }

}
