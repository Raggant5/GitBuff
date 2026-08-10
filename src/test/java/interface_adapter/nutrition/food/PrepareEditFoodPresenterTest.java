package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import interface_adapter.nutrition.meal.MealEditorViewModel;
import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodOutputData;

/**
 * Unit tests for the Prepare Edit Food Presenter.
 */
class PrepareEditFoodPresenterTest {

    @Test
    void prepareSuccessViewWithNonZeroQuantityDerivesPerServingAmounts() {
        final FoodEditorViewModel foodEditorViewModel = new FoodEditorViewModel();
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        final PrepareEditFoodPresenter presenter =
                new PrepareEditFoodPresenter(foodEditorViewModel, mealEditorViewModel);

        final PrepareEditFoodOutputData outputData = new PrepareEditFoodOutputData(8, "Pasta",
                new FoodNutritionData(400, 16, 80, 4), 2, FoodUnit.GRAM, 200);

        presenter.prepareSuccessView(outputData);

        final FoodEditorState state = foodEditorViewModel.getState();
        assertEquals(8, state.getEditingFoodEntryId());
        assertEquals("Pasta", state.getFoodName());

        final FoodServingDetails servingDetails = state.getServingDetails();
        assertEquals("2.0", servingDetails.getQuantity());
        assertEquals(FoodUnitOption.GRAM, servingDetails.getUnit());
        assertEquals(100.0, servingDetails.getServingGrams());
        assertEquals(200.0, servingDetails.getServingCalories());
        assertEquals(8.0, servingDetails.getServingProtein());
        assertEquals(40.0, servingDetails.getServingCarbs());
        assertEquals(2.0, servingDetails.getServingFat());
        assertEquals(100.0, servingDetails.getOriginalServingGrams());
        assertTrue(mealEditorViewModel.getState().getShowFoodEditor());
    }

    @Test
    void prepareSuccessViewWithZeroQuantityDoesNotDivideByZero() {
        final FoodEditorViewModel foodEditorViewModel = new FoodEditorViewModel();
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        final PrepareEditFoodPresenter presenter =
                new PrepareEditFoodPresenter(foodEditorViewModel, mealEditorViewModel);

        final PrepareEditFoodOutputData outputData = new PrepareEditFoodOutputData(9, "Soup",
                new FoodNutritionData(0, 0, 0, 0), 0, FoodUnit.GRAM, 0);

        presenter.prepareSuccessView(outputData);

        final FoodServingDetails servingDetails = foodEditorViewModel.getState().getServingDetails();
        assertEquals(0.0, servingDetails.getServingCalories());
        assertEquals(0.0, servingDetails.getServingProtein());
        assertEquals(0.0, servingDetails.getServingCarbs());
        assertEquals(0.0, servingDetails.getServingFat());
        assertEquals(0.0, servingDetails.getServingGrams());
        assertEquals(0.0, servingDetails.getOriginalServingGrams());
    }

    @Test
    void prepareSuccessViewResetsPreviousFoodEditorStateBeforeApplyingNewValues() {
        final FoodEditorViewModel foodEditorViewModel = new FoodEditorViewModel();
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        foodEditorViewModel.getState().setCaloriesError("stale error");
        final PrepareEditFoodPresenter presenter =
                new PrepareEditFoodPresenter(foodEditorViewModel, mealEditorViewModel);

        final PrepareEditFoodOutputData outputData = new PrepareEditFoodOutputData(1, "Egg",
                new FoodNutritionData(70, 6, 1, 5), 1, FoodUnit.GRAM, 50);

        presenter.prepareSuccessView(outputData);

        assertEquals("", foodEditorViewModel.getState().getCaloriesError());
    }
}
