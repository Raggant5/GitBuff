package interface_adapter.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealInputBoundary;
import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealInputData;

/**
 * Unit tests for the Prepare Edit Meal Controller.
 */
class PrepareEditMealControllerTest {

    private static final int MEAL_ID = 11;

    @Test
    void executeDelegatesMealIdToInteractor() {
        final FakePrepareEditMealInputBoundary interactor = new FakePrepareEditMealInputBoundary();
        final PrepareEditMealController controller = new PrepareEditMealController(interactor);

        controller.execute(MEAL_ID);

        assertTrue(interactor.executeCalled);
        assertEquals(MEAL_ID, interactor.receivedInputData.getMealId());
    }

    private static final class FakePrepareEditMealInputBoundary implements PrepareEditMealInputBoundary {
        private boolean executeCalled;
        private PrepareEditMealInputData receivedInputData;

        @Override
        public void execute(final PrepareEditMealInputData inputData) {
            this.executeCalled = true;
            this.receivedInputData = inputData;
        }
    }
}
