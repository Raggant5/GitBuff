package interface_adapter.log_workout.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.log_workout.logged_workout.get_workouts.GetWorkoutsInputBoundary;
import use_case.log_workout.logged_workout.get_workouts.GetWorkoutsInputData;

/**
 * Unit tests for the Get Workouts Controller.
 */
class GetWorkoutsControllerTest {

    @Test
    void executeDelegatesToInteractorWhenUsernameIsPresent() {
        final FakeGetWorkoutsInteractor interactor = new FakeGetWorkoutsInteractor();
        final GetWorkoutsController controller = new GetWorkoutsController(interactor);

        controller.execute("aahir");

        assertTrue(interactor.executeCalled);
        assertEquals("aahir", interactor.receivedInputData.getUserId());
    }

    @Test
    void executeDoesNothingWhenUsernameIsNull() {
        final FakeGetWorkoutsInteractor interactor = new FakeGetWorkoutsInteractor();
        final GetWorkoutsController controller = new GetWorkoutsController(interactor);

        controller.execute(null);

        assertFalse(interactor.executeCalled);
    }

    @Test
    void executeDoesNothingWhenUsernameIsBlank() {
        final FakeGetWorkoutsInteractor interactor = new FakeGetWorkoutsInteractor();
        final GetWorkoutsController controller = new GetWorkoutsController(interactor);

        controller.execute("   ");

        assertFalse(interactor.executeCalled);
    }

    private static final class FakeGetWorkoutsInteractor implements GetWorkoutsInputBoundary {
        private boolean executeCalled;
        private GetWorkoutsInputData receivedInputData;

        @Override
        public void execute(GetWorkoutsInputData inputData) {
            this.executeCalled = true;
            this.receivedInputData = inputData;
        }
    }
}
