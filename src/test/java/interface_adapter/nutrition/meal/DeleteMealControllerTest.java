package interface_adapter.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.nutrition.meal.delete_meal.DeleteMealInputBoundary;
import use_case.nutrition.meal.delete_meal.DeleteMealInputData;

/**
 * Unit tests for the Delete Meal Controller.
 */
class DeleteMealControllerTest {

    private static final int MEAL_ID = 42;

    @Test
    void executeDelegatesMealIdToInteractor() {
        final FakeDeleteMealInputBoundary interactor = new FakeDeleteMealInputBoundary();
        final DeleteMealController controller = new DeleteMealController(interactor);

        controller.execute(MEAL_ID);

        assertTrue(interactor.executeCalled);
        assertEquals(MEAL_ID, interactor.receivedInputData.getMealId());
    }

    private static final class FakeDeleteMealInputBoundary implements DeleteMealInputBoundary {
        private boolean executeCalled;
        private DeleteMealInputData receivedInputData;

        @Override
        public void execute(final DeleteMealInputData deleteMealInputData) {
            this.executeCalled = true;
            this.receivedInputData = deleteMealInputData;
        }
    }
}
