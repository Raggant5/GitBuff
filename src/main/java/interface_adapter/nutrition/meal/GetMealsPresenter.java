package interface_adapter.nutrition.meal;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.nutrition.food.FoodEntryDisplayData;
import interface_adapter.nutrition.food.FoodEnumMapper;
import interface_adapter.nutrition.food.FoodNutritionDisplayData;
import use_case.nutrition.food.FoodEntryData;
import use_case.nutrition.meal.MealData;
import use_case.nutrition.meal.get_meals.GetMealsOutputBoundary;
import use_case.nutrition.meal.get_meals.GetMealsOutputData;

/**
 * Presenter for the Get Meals Use Case.
 */
public class GetMealsPresenter implements GetMealsOutputBoundary {

    private final ViewMealsViewModel viewMealsViewModel;

    public GetMealsPresenter(final ViewMealsViewModel viewMealsViewModel) {
        this.viewMealsViewModel = viewMealsViewModel;
    }

    @Override
    public void prepareSuccessView(final GetMealsOutputData outputData) {
        final ViewMealsState state = this.viewMealsViewModel.getState();
        state.setMeals(toDisplayData(outputData.getMeals()));
        state.setError("");
        this.viewMealsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(final String errorMessage) {
        final ViewMealsState state = this.viewMealsViewModel.getState();
        state.setError(errorMessage);
        this.viewMealsViewModel.firePropertyChanged();
    }

    private List<MealDisplayData> toDisplayData(final List<MealData> meals) {
        final List<MealDisplayData> result = new ArrayList<>();
        for (final MealData meal : meals) {
            result.add(toDisplayData(meal));
        }
        return result;
    }

    private MealDisplayData toDisplayData(final MealData meal) {
        final List<FoodEntryDisplayData> foodEntries = new ArrayList<>();
        for (final FoodEntryData food : meal.getFoodEntries()) {
            foodEntries.add(toDisplayData(food));
        }
        return new MealDisplayData(meal.getId(), meal.getDate(), meal.getName(), foodEntries);
    }

    private FoodEntryDisplayData toDisplayData(final FoodEntryData food) {
        final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData(
                food.getNutrition().getCalories(), food.getNutrition().getProtein(),
                food.getNutrition().getCarbs(), food.getNutrition().getFat());
        return new FoodEntryDisplayData(food.getId(), food.getFoodName(), nutrition,
                food.getQuantity(), FoodEnumMapper.toOption(food.getUnit()), food.getGrams());
    }
}
