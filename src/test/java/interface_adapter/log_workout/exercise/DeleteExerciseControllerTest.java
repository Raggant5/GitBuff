package interface_adapter.log_workout.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseInputBoundary;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseInputData;

/**
 * Unit tests for the Delete Exercise Controller.
 */
class DeleteExerciseControllerTest {

    @Test
    void executeBuildsInputDataAndDelegatesToInteractor() {
        final FakeDeleteExerciseInputBoundary interactor = new FakeDeleteExerciseInputBoundary();
        final DeleteExerciseController controller = new DeleteExerciseController(interactor);

        controller.execute(7);

        assertTrue(interactor.executeCalled);
        assertEquals(7, interactor.receivedInputData.getId());
    }

    private static final class FakeDeleteExerciseInputBoundary implements DeleteExerciseInputBoundary {
        private boolean executeCalled;
        private DeleteExerciseInputData receivedInputData;

        @Override
        public void execute(final DeleteExerciseInputData deleteExerciseInputData) {
            this.executeCalled = true;
            this.receivedInputData = deleteExerciseInputData;
        }
    }
}
