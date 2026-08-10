package interface_adapter.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import interface_adapter.MainViewManagerModel;
import interface_adapter.nutrition.food.FoodUnitOption;
import use_case.nutrition.food.FoodEntryData;
import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.meal.add_meal.AddMealOutputData;

/**
 * Unit tests for the Add Meal Presenter.
 */
class AddMealPresenterTest {

    private static final int MEAL_ID = 7;

    @Test
    void prepareSuccessViewResetsEditorAddsMealAndSwitchesToNutritionView() {
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        mealEditorViewModel.getState().setName("draft");
        mealEditorViewModel.getState().setErrorMessage("stale error");
        final ViewMealsViewModel viewMealsViewModel = new ViewMealsViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();

        final AddMealPresenter presenter =
                new AddMealPresenter(mealEditorViewModel, viewMealsViewModel, mainViewManagerModel);

        final FoodNutritionData nutrition = new FoodNutritionData(100, 10, 20, 5);
        final FoodEntryData food = new FoodEntryData(1, "Egg", nutrition, 2, FoodUnit.GRAM, 100);
        final AddMealOutputData outputData =
                new AddMealOutputData(MEAL_ID, "aahir", LocalDate.of(2024, 1, 1), "Breakfast", List.of(food));

        presenter.prepareSuccessView(outputData);

        final MealEditorState editorState = mealEditorViewModel.getState();
        assertEquals("", editorState.getName());
        assertEquals("", editorState.getErrorMessage());
        assertNull(editorState.getEditingMealId());

        final ViewMealsState viewMealsState = viewMealsViewModel.getState();
        assertEquals(1, viewMealsState.getMeals().size());
        final MealDisplayData savedMeal = viewMealsState.getMeals().get(0);
        assertEquals(MEAL_ID, savedMeal.getId());
        assertEquals("Breakfast", savedMeal.getName());
        assertEquals(LocalDate.of(2024, 1, 1), savedMeal.getDate());
        assertEquals(1, savedMeal.getFoodEntries().size());
        assertEquals("Egg", savedMeal.getFoodEntries().get(0).getFoodName());
        assertEquals(FoodUnitOption.GRAM, savedMeal.getFoodEntries().get(0).getUnit());
        assertEquals("100.0", savedMeal.getFoodEntries().get(0).getNutrition().getCalories());

        assertEquals("nutrition", mainViewManagerModel.getState());
    }

    @Test
    void prepareFailViewSetsErrorMessageOnMealEditorState() {
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        final ViewMealsViewModel viewMealsViewModel = new ViewMealsViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
        final AddMealPresenter presenter =
                new AddMealPresenter(mealEditorViewModel, viewMealsViewModel, mainViewManagerModel);

        presenter.prepareFailView("Meal name is required.");

        assertEquals("Meal name is required.", mealEditorViewModel.getState().getErrorMessage());
        assertTrue(viewMealsViewModel.getState().getMeals().isEmpty());
    }
}
