package interface_adapter.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import interface_adapter.nutrition.food.FoodEntryDisplayData;
import interface_adapter.nutrition.food.FoodUnitOption;
import use_case.nutrition.food.FoodEntryData;
import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.meal.MealData;
import use_case.nutrition.meal.get_meals.GetMealsOutputData;

/**
 * Unit tests for the Get Meals Presenter.
 */
class GetMealsPresenterTest {

    private static final int MEAL_ID = 1;

    @Test
    void prepareSuccessViewMapsMealsToDisplayDataAndClearsError() {
        final ViewMealsViewModel viewMealsViewModel = new ViewMealsViewModel();
        viewMealsViewModel.getState().setError("stale error");
        final GetMealsPresenter presenter = new GetMealsPresenter(viewMealsViewModel);

        final FoodNutritionData nutrition = new FoodNutritionData(100, 10, 20, 5);
        final FoodEntryData food = new FoodEntryData(1, "Toast", nutrition, 1, FoodUnit.DEFAULT_SERVING, 50);
        final MealData meal = new MealData(MEAL_ID, "aahir", LocalDate.of(2024, 3, 3), "Breakfast", List.of(food));

        presenter.prepareSuccessView(new GetMealsOutputData(List.of(meal)));

        final ViewMealsState state = viewMealsViewModel.getState();
        assertEquals("", state.getError());
        assertEquals(1, state.getMeals().size());

        final MealDisplayData mealDisplay = state.getMeals().get(0);
        assertEquals(MEAL_ID, mealDisplay.getId());
        assertEquals("Breakfast", mealDisplay.getName());
        assertEquals(LocalDate.of(2024, 3, 3), mealDisplay.getDate());
        assertEquals(1, mealDisplay.getFoodEntries().size());

        final FoodEntryDisplayData foodDisplay = mealDisplay.getFoodEntries().get(0);
        assertEquals("Toast", foodDisplay.getFoodName());
        assertEquals(FoodUnitOption.DEFAULT_SERVING, foodDisplay.getUnit());
        assertEquals("100.0", foodDisplay.getNutrition().getCalories());
    }

    @Test
    void prepareSuccessViewWithNoMealsProducesEmptyList() {
        final ViewMealsViewModel viewMealsViewModel = new ViewMealsViewModel();
        final GetMealsPresenter presenter = new GetMealsPresenter(viewMealsViewModel);

        presenter.prepareSuccessView(new GetMealsOutputData(Collections.emptyList()));

        assertTrue(viewMealsViewModel.getState().getMeals().isEmpty());
    }

    @Test
    void prepareFailViewSetsErrorMessage() {
        final ViewMealsViewModel viewMealsViewModel = new ViewMealsViewModel();
        final GetMealsPresenter presenter = new GetMealsPresenter(viewMealsViewModel);

        presenter.prepareFailView("Failed to load meals.");

        assertEquals("Failed to load meals.", viewMealsViewModel.getState().getError());
    }
}
