package interface_adapter.log_workout.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.log_workout.exercise_performed.prepare_edit_exercise.PrepareEditExerciseInputBoundary;
import use_case.log_workout.exercise_performed.prepare_edit_exercise.PrepareEditExerciseInputData;

/**
 * Unit tests for the Prepare Edit Exercise Controller.
 */
class PrepareEditExerciseControllerTest {

    @Test
    void executeBuildsInputDataAndDelegatesToInteractor() {
        final FakePrepareEditExerciseInputBoundary interactor = new FakePrepareEditExerciseInputBoundary();
        final PrepareEditExerciseController controller = new PrepareEditExerciseController(interactor);
        final StrengthDetailsDisplayData strengthDetailsDisplayData =
                new StrengthDetailsDisplayData("5", "5", "100.0");
        final ExercisePerformedDisplayData exercisePerformed = new ExercisePerformedDisplayData(
                11, "Deadlift", strengthDetailsDisplayData, 0.0, null, false);

        controller.execute(exercisePerformed);

        assertTrue(interactor.executeCalled);
        assertEquals(11, interactor.receivedInputData.getId());
        assertEquals("Deadlift", interactor.receivedInputData.getExerciseName());
        assertEquals("5", interactor.receivedInputData.getStrengthDetailsInput().getSets());
        assertEquals("5", interactor.receivedInputData.getStrengthDetailsInput().getReps());
        assertEquals("100.0", interactor.receivedInputData.getStrengthDetailsInput().getWeight());
        assertEquals(0.0, interactor.receivedInputData.getDurationMins());
        assertEquals(null, interactor.receivedInputData.getDistanceKm());
        assertEquals(false, interactor.receivedInputData.getIsCardio());
    }

    private static final class FakePrepareEditExerciseInputBoundary
            implements PrepareEditExerciseInputBoundary {
        private boolean executeCalled;
        private PrepareEditExerciseInputData receivedInputData;

        @Override
        public void execute(final PrepareEditExerciseInputData inputData) {
            this.executeCalled = true;
            this.receivedInputData = inputData;
        }
    }
}
