package interface_adapter.nutrition.meal;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.MainViewManagerModel;
import interface_adapter.nutrition.food.FoodEntryDisplayData;
import interface_adapter.nutrition.food.FoodNutritionDisplayData;
import use_case.nutrition.food.FoodEntryData;
import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealOutputBoundary;
import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealOutputData;

public class PrepareEditMealPresenter implements PrepareEditMealOutputBoundary {
    private final MealEditorViewModel mealEditorViewModel;
    private final ViewMealsViewModel viewMealsViewModel;
    private final MainViewManagerModel mainViewManagerModel;

    public PrepareEditMealPresenter(MealEditorViewModel mealEditorViewModel, ViewMealsViewModel viewMealsViewModel,
                                    MainViewManagerModel mainViewManagerModel) {
        this.mealEditorViewModel = mealEditorViewModel;
        this.viewMealsViewModel = viewMealsViewModel;
        this.mainViewManagerModel = mainViewManagerModel;
    }

    @Override
    public void prepareSuccessView(PrepareEditMealOutputData outputData) {
        final List<FoodEntryDisplayData> foodEntries = new ArrayList<>();
        for (FoodEntryData food : outputData.getFoodEntries()) {
            final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData(food.getNutrition().getCalories(),
                    food.getNutrition().getProtein(), food.getNutrition().getCarbs(), food.getNutrition().getFat());
            foodEntries.add(new FoodEntryDisplayData(food.getId(), food.getFoodName(), nutrition,
                    food.getQuantity(), food.getUnit(), food.getGrams()));
        }

        final MealEditorState mealEditorState = mealEditorViewModel.getState();
        mealEditorState.reset();
        mealEditorState.setEditingMealId(outputData.getMealId());
        mealEditorState.setDate(outputData.getDate());
        mealEditorState.setName(outputData.getName());
        mealEditorState.setErrorMessage("");
        mealEditorState.setFoodEntriesForMeal(foodEntries);

        mainViewManagerModel.setState("meal editor");
        mainViewManagerModel.firePropertyChanged();
        mealEditorViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewMealsViewModel.getState().setError(errorMessage);
        viewMealsViewModel.firePropertyChanged();
    }

}
