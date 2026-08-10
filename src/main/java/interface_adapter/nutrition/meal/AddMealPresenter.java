package interface_adapter.nutrition.meal;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.MainViewManagerModel;
import interface_adapter.nutrition.food.FoodEnumMapper;
import interface_adapter.nutrition.food.FoodEntryDisplayData;
import interface_adapter.nutrition.food.FoodNutritionDisplayData;
import use_case.nutrition.food.FoodEntryData;
import use_case.nutrition.meal.add_meal.AddMealOutputBoundary;
import use_case.nutrition.meal.add_meal.AddMealOutputData;

/**
 * Presenter for the Add Meal Use Case.
 */
public class AddMealPresenter implements AddMealOutputBoundary {

    private final MealEditorViewModel mealEditorViewModel;
    private final ViewMealsViewModel viewMealsViewModel;
    private final MainViewManagerModel mainViewManagerModel;

    /**
     * Constructs an AddMealPresenter.
     *
     * @param mealEditorViewModel meal editor view model
     * @param viewMealsViewModel saved meals view model
     * @param mainViewManagerModel main view manager model
     */
    public AddMealPresenter(
            final MealEditorViewModel mealEditorViewModel,
            final ViewMealsViewModel viewMealsViewModel,
            final MainViewManagerModel mainViewManagerModel
    ) {
        this.mealEditorViewModel = mealEditorViewModel;
        this.viewMealsViewModel = viewMealsViewModel;
        this.mainViewManagerModel = mainViewManagerModel;
    }

    @Override
    public void prepareSuccessView(
            final AddMealOutputData outputData
    ) {
        final List<FoodEntryDisplayData> foodEntries = new ArrayList<>();
        for (FoodEntryData food : outputData.getFoodEntries()) {
            final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData(food.getNutrition().getCalories(),
                    food.getNutrition().getProtein(), food.getNutrition().getCarbs(), food.getNutrition().getFat());
            foodEntries.add(new FoodEntryDisplayData(food.getId(), food.getFoodName(), nutrition,
                    food.getQuantity(), FoodEnumMapper.toOption(food.getUnit()), food.getGrams()));
        }
        final MealDisplayData savedMeal = new MealDisplayData(outputData.getId(), outputData.getDate(),
                outputData.getName(), foodEntries);

        final MealEditorState currentState =
                this.mealEditorViewModel.getState();

        currentState.reset();
        this.mealEditorViewModel.firePropertyChanged();

        final ViewMealsState mealsState =
                this.viewMealsViewModel.getState();

        mealsState.addMeal(savedMeal);
        this.viewMealsViewModel.setState(mealsState);
        this.viewMealsViewModel.firePropertyChanged();

        this.mainViewManagerModel.setState("nutrition");
        this.mainViewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(
            final String errorMessage
    ) {
        this.mealEditorViewModel
                .getState()
                .setErrorMessage(errorMessage);

        this.mealEditorViewModel.firePropertyChanged();
    }
}
