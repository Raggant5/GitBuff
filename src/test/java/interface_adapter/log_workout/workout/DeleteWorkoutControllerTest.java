package interface_adapter.log_workout.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutInputBoundary;
import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutInputData;

/**
 * Unit tests for the Delete Workout Controller.
 */
class DeleteWorkoutControllerTest {

    @Test
    void executeDelegatesWorkoutIdToInteractor() {
        final FakeDeleteWorkoutInteractor interactor = new FakeDeleteWorkoutInteractor();
        final DeleteWorkoutController controller = new DeleteWorkoutController(interactor);

        controller.execute(42);

        assertTrue(interactor.executeCalled);
        assertEquals(42, interactor.receivedInputData.getWorkoutId());
    }

    private static final class FakeDeleteWorkoutInteractor implements DeleteWorkoutInputBoundary {
        private boolean executeCalled;
        private DeleteWorkoutInputData receivedInputData;

        @Override
        public void execute(DeleteWorkoutInputData deleteWorkoutInputData) {
            this.executeCalled = true;
            this.receivedInputData = deleteWorkoutInputData;
        }
    }
}
