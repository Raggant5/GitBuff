package interface_adapter.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.nutrition.meal.get_meals.GetMealsInputBoundary;
import use_case.nutrition.meal.get_meals.GetMealsInputData;

/**
 * Unit tests for the Get Meals Controller.
 */
class GetMealsControllerTest {

    @Test
    void executeCallsInteractorWhenUsernamePresent() {
        final FakeGetMealsInputBoundary interactor = new FakeGetMealsInputBoundary();
        final GetMealsController controller = new GetMealsController(interactor);

        controller.execute("aahir");

        assertTrue(interactor.executeCalled);
        assertEquals("aahir", interactor.receivedInputData.getUserId());
    }

    @Test
    void executeDoesNothingWhenUsernameIsNull() {
        final FakeGetMealsInputBoundary interactor = new FakeGetMealsInputBoundary();
        final GetMealsController controller = new GetMealsController(interactor);

        controller.execute(null);

        assertFalse(interactor.executeCalled);
    }

    @Test
    void executeDoesNothingWhenUsernameIsBlank() {
        final FakeGetMealsInputBoundary interactor = new FakeGetMealsInputBoundary();
        final GetMealsController controller = new GetMealsController(interactor);

        controller.execute("   ");

        assertFalse(interactor.executeCalled);
    }

    private static final class FakeGetMealsInputBoundary implements GetMealsInputBoundary {
        private boolean executeCalled;
        private GetMealsInputData receivedInputData;

        @Override
        public void execute(final GetMealsInputData inputData) {
            this.executeCalled = true;
            this.receivedInputData = inputData;
        }
    }
}
