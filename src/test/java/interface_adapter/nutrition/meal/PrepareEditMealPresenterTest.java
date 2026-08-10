package interface_adapter.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import interface_adapter.MainViewManagerModel;
import interface_adapter.nutrition.food.FoodUnitOption;
import use_case.nutrition.food.FoodEntryData;
import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealOutputData;

/**
 * Unit tests for the Prepare Edit Meal Presenter.
 */
class PrepareEditMealPresenterTest {

    private static final int MEAL_ID = 4;

    @Test
    void prepareSuccessViewResetsThenPopulatesEditorStateAndSwitchesToMealEditorView() {
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        mealEditorViewModel.getState().setShowFoodEditor(true);
        mealEditorViewModel.getState().setErrorMessage("stale error");
        final ViewMealsViewModel viewMealsViewModel = new ViewMealsViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();

        final PrepareEditMealPresenter presenter =
                new PrepareEditMealPresenter(mealEditorViewModel, viewMealsViewModel, mainViewManagerModel);

        final FoodNutritionData nutrition = new FoodNutritionData(100, 10, 20, 5);
        final FoodEntryData food = new FoodEntryData(1, "Egg", nutrition, 2, FoodUnit.GRAM, 100);
        final PrepareEditMealOutputData outputData =
                new PrepareEditMealOutputData(MEAL_ID, LocalDate.of(2024, 4, 4), "Brunch", List.of(food));

        presenter.prepareSuccessView(outputData);

        final MealEditorState state = mealEditorViewModel.getState();
        assertEquals(MEAL_ID, state.getEditingMealId());
        assertEquals(LocalDate.of(2024, 4, 4), state.getDate());
        assertEquals("Brunch", state.getName());
        assertEquals("", state.getErrorMessage());
        assertFalse(state.getShowFoodEditor());
        assertEquals(1, state.getFoodEntriesForMeal().size());
        assertEquals("Egg", state.getFoodEntriesForMeal().get(0).getFoodName());
        assertEquals(FoodUnitOption.GRAM, state.getFoodEntriesForMeal().get(0).getUnit());

        assertEquals("meal editor", mainViewManagerModel.getState());
    }

    @Test
    void prepareFailViewSetsErrorMessageOnViewMealsState() {
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        final ViewMealsViewModel viewMealsViewModel = new ViewMealsViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
        final PrepareEditMealPresenter presenter =
                new PrepareEditMealPresenter(mealEditorViewModel, viewMealsViewModel, mainViewManagerModel);

        presenter.prepareFailView("Meal not found.");

        assertEquals("Meal not found.", viewMealsViewModel.getState().getError());
    }
}
