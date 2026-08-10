package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import use_case.nutrition.food.edit_food.EditFoodInputBoundary;
import use_case.nutrition.food.edit_food.EditFoodInputData;

/**
 * Unit tests for the Edit Food Controller.
 */
class EditFoodControllerTest {

    @Test
    void executeDelegatesToInteractorWithConvertedInputData() {
        final FakeEditFoodInteractor interactor = new FakeEditFoodInteractor();
        final EditFoodController controller = new EditFoodController(interactor);

        final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData("300", "20", "40", "10");

        controller.execute(9, "Chicken", nutrition, "3", FoodUnitOption.TEASPOON, "250");

        assertTrue(interactor.executeCalled);
        final EditFoodInputData received = interactor.receivedInputData;
        assertEquals(9, received.getId());
        assertEquals("Chicken", received.getFoodName());
        assertEquals("300", received.getNutrition().getCalories());
        assertEquals("20", received.getNutrition().getProtein());
        assertEquals("40", received.getNutrition().getCarbs());
        assertEquals("10", received.getNutrition().getFat());
        assertEquals("3", received.getQuantity());
        assertEquals(FoodUnit.TEASPOON, received.getUnit());
        assertEquals("250", received.getGrams());
    }

    private static final class FakeEditFoodInteractor implements EditFoodInputBoundary {
        private boolean executeCalled;
        private EditFoodInputData receivedInputData;

        @Override
        public void execute(EditFoodInputData inputData) {
            this.executeCalled = true;
            this.receivedInputData = inputData;
        }
    }
}
