package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import interface_adapter.nutrition.meal.MealEditorState;
import interface_adapter.nutrition.meal.MealEditorViewModel;
import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.food.FoodValidationErrors;
import use_case.nutrition.food.edit_food.EditFoodOutputData;

/**
 * Unit tests for the Edit Food Presenter.
 */
class EditFoodPresenterTest {

    @Test
    void prepareSuccessViewReplacesFoodEntryAndClosesEditor() {
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        final FoodEditorViewModel foodEditorViewModel = new FoodEditorViewModel();
        final EditFoodPresenter presenter = new EditFoodPresenter(mealEditorViewModel, foodEditorViewModel);

        final MealEditorState mealState = mealEditorViewModel.getState();
        final FoodNutritionDisplayData originalNutrition = new FoodNutritionDisplayData("100", "5", "10", "2");
        mealState.addFoodEntry(new FoodEntryDisplayData(5, "Old Name", originalNutrition, 1, FoodUnitOption.GRAM, 90));
        mealState.setShowFoodEditor(true);
        foodEditorViewModel.getState().setFoodName("mid-edit");

        final EditFoodOutputData outputData = new EditFoodOutputData(5, "New Name",
                new FoodNutritionData(150, 8, 12, 3), 2, FoodUnit.CUP, 200);

        presenter.prepareSuccessView(outputData);

        assertEquals(1, mealState.getFoodEntriesForMeal().size());
        final FoodEntryDisplayData updated = mealState.getFoodEntriesForMeal().get(0);
        assertEquals("New Name", updated.getFoodName());
        assertEquals(FoodUnitOption.CUP, updated.getUnit());
        assertFalse(mealState.getShowFoodEditor());
        assertEquals("", foodEditorViewModel.getState().getFoodName());
    }

    @Test
    void prepareFailViewSetsErrorsOnFoodEditorState() {
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        final FoodEditorViewModel foodEditorViewModel = new FoodEditorViewModel();
        final EditFoodPresenter presenter = new EditFoodPresenter(mealEditorViewModel, foodEditorViewModel);

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
        assertEquals("bad calories", state.getCaloriesError());
        assertEquals("bad protein", state.getProteinError());
        assertEquals("bad carbs", state.getCarbsError());
        assertEquals("bad fat", state.getFatError());
        assertEquals("bad quantity", state.getQuantityError());
        assertEquals("bad grams", state.getGramsError());
        assertEquals("general failure", state.getSubmitError());
    }
}
