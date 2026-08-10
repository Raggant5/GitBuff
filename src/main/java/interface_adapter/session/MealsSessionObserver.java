package interface_adapter.session;

import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import entity.Meal;
import interface_adapter.calendar.CalendarSyncObserver;
import interface_adapter.nutrition.food.FoodEntryDisplayData;
import interface_adapter.nutrition.food.FoodNutritionDisplayData;
import interface_adapter.nutrition.meal.MealDisplayData;
import interface_adapter.nutrition.meal.ViewMealsViewModel;

/**
 * Reloads the logged-in user's saved meals into the meals view.
 *
 * <p>Extracted from {@code interface_adapter.login.LoginPresenter}, which previously did this
 * inline as one of several unrelated responsibilities crammed into one method.
 */
public class MealsSessionObserver implements UserSessionObserver {

    private final ViewMealsViewModel mealsViewModel;

    /**
     * Constructs a MealsSessionObserver instance.
     *
     * @param mealsViewModel view model for the user's saved meals
     */
    public MealsSessionObserver(final ViewMealsViewModel mealsViewModel) {
        this.mealsViewModel = mealsViewModel;
    }

    @Override
    public void onUserLoggedIn(final UserLoggedInEvent event) {
        final List<MealDisplayData> meals = toDisplayData(event.data().getMeals());
        this.mealsViewModel.getState().setMeals(meals);
        this.mealsViewModel.firePropertyChanged();
    }

    /**
     * Converts saved meals into display data. Public and static so
     * {@link CalendarSyncObserver} can reuse the exact same conversion without depending on
     * this observer having already run.
     *
     * @param meals the user's saved meals
     * @return the meals converted to display data
     */
    public static List<MealDisplayData> toDisplayData(final List<Meal> meals) {
        final List<MealDisplayData> result = new ArrayList<>();
        for (final Meal meal : meals) {
            final List<FoodEntryDisplayData> foodEntries = new ArrayList<>();
            for (final FoodEntry food : meal.getFoodEntries()) {
                final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData(
                        food.getNutrition().getCalories(), food.getNutrition().getProtein(),
                        food.getNutrition().getCarbs(), food.getNutrition().getFat());
                foodEntries.add(new FoodEntryDisplayData(food.getId(), food.getFoodName(), nutrition,
                        food.getQuantity(), food.getUnit(), food.getGrams()));
            }
            result.add(new MealDisplayData(meal.getId(), meal.getDate(), meal.getName(), foodEntries));
        }
        return result;
    }
}

