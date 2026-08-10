package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import use_case.nutrition.food.change_serving_size.ChangeServingSizeInputBoundary;
import use_case.nutrition.food.change_serving_size.ChangeServingSizeInputData;

/**
 * Unit tests for the Change Serving Size Controller.
 */
class ChangeServingSizeControllerTest {

    @Test
    void executeDelegatesToInteractorWithConvertedInputData() {
        final FakeChangeServingSizeInteractor interactor = new FakeChangeServingSizeInteractor();
        final ChangeServingSizeController controller = new ChangeServingSizeController(interactor);

        controller.execute(FoodUnitOption.CUP, 50.0, 100.0, 200.0, 10.0, 20.0, 5.0);

        assertTrue(interactor.executeCalled);
        final ChangeServingSizeInputData received = interactor.receivedInputData;
        assertEquals(FoodUnit.CUP, received.getUnit());
        assertEquals(50.0, received.getOriginalServingGrams());
        assertEquals(100.0, received.getServingGrams());
        assertEquals(200.0, received.getServingCalories());
        assertEquals(10.0, received.getServingProtein());
        assertEquals(20.0, received.getServingCarbs());
        assertEquals(5.0, received.getServingFat());
    }

    private static final class FakeChangeServingSizeInteractor implements ChangeServingSizeInputBoundary {
        private boolean executeCalled;
        private ChangeServingSizeInputData receivedInputData;

        @Override
        public void execute(ChangeServingSizeInputData inputData) {
            this.executeCalled = true;
            this.receivedInputData = inputData;
        }
    }
}
