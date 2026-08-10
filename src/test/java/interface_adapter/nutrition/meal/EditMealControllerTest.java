package interface_adapter.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import interface_adapter.nutrition.food.FoodEntryDisplayData;
import interface_adapter.nutrition.food.FoodNutritionDisplayData;
import interface_adapter.nutrition.food.FoodUnitOption;
import use_case.nutrition.meal.edit_meal.EditMealInputBoundary;
import use_case.nutrition.meal.edit_meal.EditMealInputData;

/**
 * Unit tests for the Edit Meal Controller.
 */
class EditMealControllerTest {

    private static final int MEAL_ID = 3;
    private static final int FOOD_ID = 7;
    private static final double QUANTITY = 1.5;
    private static final double GRAMS = 300.0;
    private static final double DELTA = 0.0001;

    @Test
    void executeBuildsInputDataWithConvertedFoodsAndDeleteIds() {
        final FakeEditMealInputBoundary interactor = new FakeEditMealInputBoundary();
        final EditMealController controller = new EditMealController(interactor);

        final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData("100", "10", "20", "5");
        final FoodEntryDisplayData food = new FoodEntryDisplayData(FOOD_ID, "Rice", nutrition, QUANTITY,
                FoodUnitOption.CUP, GRAMS);

        controller.execute(MEAL_ID, "Dinner", List.of(food), List.of(9, 10));

        assertTrue(interactor.executeCalled);
        final EditMealInputData inputData = interactor.receivedInputData;
        assertEquals(MEAL_ID, inputData.getMealId());
        assertEquals("Dinner", inputData.getName());
        assertEquals(1, inputData.getFoodEntries().size());
        assertEquals(FOOD_ID, inputData.getFoodEntries().get(0).getId());
        assertEquals("Rice", inputData.getFoodEntries().get(0).getFoodName());
        assertEquals(FoodUnit.CUP, inputData.getFoodEntries().get(0).getUnit());
        assertEquals(QUANTITY, inputData.getFoodEntries().get(0).getQuantity(), DELTA);
        assertEquals(GRAMS, inputData.getFoodEntries().get(0).getGrams(), DELTA);
        assertEquals(List.of(9, 10), inputData.getFoodEntryIdsToDelete());
    }

    @Test
    void executeWithEmptyFoodListProducesEmptyEntries() {
        final FakeEditMealInputBoundary interactor = new FakeEditMealInputBoundary();
        final EditMealController controller = new EditMealController(interactor);

        controller.execute(1, "Snack", Collections.emptyList(), Collections.emptyList());

        assertTrue(interactor.receivedInputData.getFoodEntries().isEmpty());
        assertTrue(interactor.receivedInputData.getFoodEntryIdsToDelete().isEmpty());
    }

    private static final class FakeEditMealInputBoundary implements EditMealInputBoundary {
        private boolean executeCalled;
        private EditMealInputData receivedInputData;

        @Override
        public void execute(final EditMealInputData inputData) {
            this.executeCalled = true;
            this.receivedInputData = inputData;
        }
    }
}
