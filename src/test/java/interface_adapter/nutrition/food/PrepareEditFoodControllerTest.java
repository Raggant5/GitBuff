package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodInputBoundary;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodInputData;

/**
 * Unit tests for the Prepare Edit Food Controller.
 */
class PrepareEditFoodControllerTest {

    @Test
    void executeDelegatesToInteractorWithConvertedInputData() {
        final FakePrepareEditFoodInteractor interactor = new FakePrepareEditFoodInteractor();
        final PrepareEditFoodController controller = new PrepareEditFoodController(interactor);

        final FoodNutritionDisplayData nutrition = new FoodNutritionDisplayData("120", "8", "18", "3");
        final FoodEntryDisplayData food = new FoodEntryDisplayData(6, "Yogurt", nutrition, 2, FoodUnitOption.CUP, 480);

        controller.execute(food);

        assertTrue(interactor.executeCalled);
        final PrepareEditFoodInputData received = interactor.receivedInputData;
        assertEquals(6, received.getId());
        assertEquals("Yogurt", received.getFoodName());
        assertEquals("120", received.getNutrition().getCalories());
        assertEquals("8", received.getNutrition().getProtein());
        assertEquals("18", received.getNutrition().getCarbs());
        assertEquals("3", received.getNutrition().getFat());
        assertEquals(2, received.getQuantity());
        assertEquals(FoodUnit.CUP, received.getUnit());
        assertEquals(480, received.getGrams());
    }

    private static final class FakePrepareEditFoodInteractor implements PrepareEditFoodInputBoundary {
        private boolean executeCalled;
        private PrepareEditFoodInputData receivedInputData;

        @Override
        public void execute(PrepareEditFoodInputData inputData) {
            this.executeCalled = true;
            this.receivedInputData = inputData;
        }
    }
}
