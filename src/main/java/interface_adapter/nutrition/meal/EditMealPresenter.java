package interface_adapter.nutrition.meal;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.MainViewManagerModel;
import interface_adapter.nutrition.food.FoodEntryDisplayData;
import interface_adapter.nutrition.food.FoodEnumMapper;
import interface_adapter.nutrition.food.FoodNutritionDisplayData;
import use_case.nutrition.food.FoodEntryData;
import use_case.nutrition.meal.edit_meal.EditMealOutputBoundary;
import use_case.nutrition.meal.edit_meal.EditMealOutputData;

/**
 * Presenter for the Edit Meal Use Case.
 */
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
        final List<FoodEntryDisplayData> foodEntries = new ArrayList<>();
        for (FoodEntryData food : outputData.getFoodEntries()) {
            final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData(food.getNutrition().getCalories(),
                    food.getNutrition().getProtein(), food.getNutrition().getCarbs(), food.getNutrition().getFat());
            foodEntries.add(new FoodEntryDisplayData(food.getId(), food.getFoodName(), nutrition,
                    food.getQuantity(), FoodEnumMapper.toOption(food.getUnit()), food.getGrams()));
        }
        final MealDisplayData updatedMeal = new MealDisplayData(outputData.getId(), outputData.getDate(),
                outputData.getName(), foodEntries);

        final ViewMealsState viewMealsState = viewMealsViewModel.getState();
        viewMealsState.replaceMeal(updatedMeal);
        viewMealsViewModel.firePropertyChanged();
        mealEditorViewModel.getState().reset();
        mealEditorViewModel.firePropertyChanged();

        mainViewManagerModel.setState("nutrition");
        mainViewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {

        final MealEditorState state = mealEditorViewModel.getState();
        state.setErrorMessage(errorMessage);
        mealEditorViewModel.firePropertyChanged();

    }
}
