package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import interface_adapter.nutrition.meal.MealEditorState;
import interface_adapter.nutrition.meal.MealEditorViewModel;
import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.food.FoodValidationErrors;
import use_case.nutrition.food.create_food.AddFoodEntryOutputData;

/**
 * Unit tests for the Add Food Presenter.
 */
class AddFoodPresenterTest {

    @Test
    void prepareSuccessViewWithNullIdAssignsNextTempId() {
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        final FoodEditorViewModel foodEditorViewModel = new FoodEditorViewModel();
        final AddFoodPresenter presenter = new AddFoodPresenter(mealEditorViewModel, foodEditorViewModel);

        final MealEditorState mealState = mealEditorViewModel.getState();
        mealState.setNextTempId(-1);

        final AddFoodEntryOutputData outputData = new AddFoodEntryOutputData(null, "Apple",
                new FoodNutritionData(100, 10, 20, 5), 1, FoodUnit.GRAM, 150);

        presenter.prepareSuccessView(outputData);

        assertEquals(1, mealState.getFoodEntriesForMeal().size());
        final FoodEntryDisplayData added = mealState.getFoodEntriesForMeal().get(0);
        assertEquals(-1, added.getId());
        assertEquals("Apple", added.getFoodName());
        assertEquals(FoodUnitOption.GRAM, added.getUnit());
        assertEquals(-2, mealState.getNextTempId());
        assertFalse(mealState.getShowFoodEditor());
    }

    @Test
    void prepareSuccessViewWithExistingIdUsesGivenId() {
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        final FoodEditorViewModel foodEditorViewModel = new FoodEditorViewModel();
        final AddFoodPresenter presenter = new AddFoodPresenter(mealEditorViewModel, foodEditorViewModel);

        final AddFoodEntryOutputData outputData = new AddFoodEntryOutputData(42, "Rice",
                new FoodNutritionData(200, 4, 45, 1), 1, FoodUnit.CUP, 158);

        presenter.prepareSuccessView(outputData);

        final FoodEntryDisplayData added = mealEditorViewModel.getState().getFoodEntriesForMeal().get(0);
        assertEquals(42, added.getId());
        assertEquals(FoodUnitOption.CUP, added.getUnit());
    }

    @Test
    void prepareSuccessViewResetsFoodEditorState() {
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        final FoodEditorViewModel foodEditorViewModel = new FoodEditorViewModel();
        final AddFoodPresenter presenter = new AddFoodPresenter(mealEditorViewModel, foodEditorViewModel);

        foodEditorViewModel.getState().setFoodName("Leftover");

        final AddFoodEntryOutputData outputData = new AddFoodEntryOutputData(1, "Toast",
                new FoodNutritionData(80, 3, 15, 1), 1, FoodUnit.GRAM, 30);

        presenter.prepareSuccessView(outputData);

        assertEquals("", foodEditorViewModel.getState().getFoodName());
    }

    @Test
    void prepareFailViewSetsErrorsOnFoodEditorState() {
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        final FoodEditorViewModel foodEditorViewModel = new FoodEditorViewModel();
        final AddFoodPresenter presenter = new AddFoodPresenter(mealEditorViewModel, foodEditorViewModel);

        final FoodValidationErrors errors = new FoodValidationErrors();
        errors.setCaloriesError("bad calories");
        errors.setProteinError("bad protein");
        errors.setCarbsError("bad carbs");
        errors.setFatError("bad fat");
        errors.setQuantityError("bad quantity");
        errors.setGramsError("bad grams");
        errors.setGeneralError("general failure");

        presenter.prepareFailView(errors);

        final FoodEditorState state = foodEditorViewModel.getState();
        assertNotNull(state);
        assertEquals("bad calories", state.getCaloriesError());
        assertEquals("bad protein", state.getProteinError());
        assertEquals("bad carbs", state.getCarbsError());
        assertEquals("bad fat", state.getFatError());
        assertEquals("bad quantity", state.getQuantityError());
        assertEquals("bad grams", state.getGramsError());
        assertEquals("general failure", state.getSubmitError());
    }
}
