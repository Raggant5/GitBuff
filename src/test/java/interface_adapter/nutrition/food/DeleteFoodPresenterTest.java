package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import interface_adapter.nutrition.meal.MealEditorState;
import interface_adapter.nutrition.meal.MealEditorViewModel;
import use_case.nutrition.food.delete_food.DeleteFoodOutputData;

/**
 * Unit tests for the Delete Food Presenter.
 */
class DeleteFoodPresenterTest {

    @Test
    void prepareSuccessViewRemovesFoodEntryAndStagesDeletion() {
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        final DeleteFoodPresenter presenter = new DeleteFoodPresenter(mealEditorViewModel);

        final MealEditorState state = mealEditorViewModel.getState();
        final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData("100", "10", "20", "5");
        state.addFoodEntry(new FoodEntryDisplayData(3, "Bread", nutrition, 1, FoodUnitOption.GRAM, 100));

        presenter.prepareSuccessView(new DeleteFoodOutputData(3));

        assertTrue(state.getFoodEntriesForMeal().isEmpty());
        assertEquals(1, state.getFoodEntriesDeleteStage().size());
        assertEquals(3, state.getFoodEntriesDeleteStage().get(0));
    }
}
