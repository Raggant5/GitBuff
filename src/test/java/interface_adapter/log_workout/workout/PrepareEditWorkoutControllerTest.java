package interface_adapter.log_workout.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutInputBoundary;
import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutInputData;

/**
 * Unit tests for the Prepare Edit Workout Controller.
 */
class PrepareEditWorkoutControllerTest {

    @Test
    void executeDelegatesWorkoutIdToInteractor() {
        final FakePrepareEditWorkoutInteractor interactor = new FakePrepareEditWorkoutInteractor();
        final PrepareEditWorkoutController controller = new PrepareEditWorkoutController(interactor);

        controller.execute(11);

        assertTrue(interactor.executeCalled);
        assertEquals(11, interactor.receivedInputData.getWorkoutId());
    }

    private static final class FakePrepareEditWorkoutInteractor implements PrepareEditWorkoutInputBoundary {
        private boolean executeCalled;
        private PrepareEditWorkoutInputData receivedInputData;

        @Override
        public void execute(PrepareEditWorkoutInputData inputData) {
            this.executeCalled = true;
            this.receivedInputData = inputData;
        }
    }
}
