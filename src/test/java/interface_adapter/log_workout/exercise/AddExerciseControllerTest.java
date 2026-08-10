package interface_adapter.log_workout.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.log_workout.exercise_performed.create_exercise.AddExercisePerformedInputBoundary;
import use_case.log_workout.exercise_performed.create_exercise.AddExercisePerformedInputData;

/**
 * Unit tests for the Add Exercise Controller.
 */
class AddExerciseControllerTest {

    @Test
    void executeBuildsInputDataAndDelegatesToInteractor() {
        final FakeAddExercisePerformedInputBoundary interactor = new FakeAddExercisePerformedInputBoundary();
        final AddExerciseController controller = new AddExerciseController(interactor);
        final StrengthDetailsDisplayData strengthDetailsDisplayData =
                new StrengthDetailsDisplayData("3", "10", "50.0");

        controller.execute("Bench Press", strengthDetailsDisplayData, false, "", "5.0");

        assertTrue(interactor.executeCalled);
        assertEquals("Bench Press", interactor.receivedInputData.getName());
        assertEquals("3", interactor.receivedInputData.getSets());
        assertEquals("10", interactor.receivedInputData.getReps());
        assertEquals("50.0", interactor.receivedInputData.getWeight());
        assertEquals(false, interactor.receivedInputData.isCardio());
        assertEquals("", interactor.receivedInputData.getDistance());
        assertEquals("5.0", interactor.receivedInputData.getDuration());
    }

    @Test
    void executeWithCardioExercise() {
        final FakeAddExercisePerformedInputBoundary interactor = new FakeAddExercisePerformedInputBoundary();
        final AddExerciseController controller = new AddExerciseController(interactor);
        final StrengthDetailsDisplayData strengthDetailsDisplayData =
                new StrengthDetailsDisplayData("", "", "");

        controller.execute("Running", strengthDetailsDisplayData, true, "5.0", "30.0");

        assertTrue(interactor.executeCalled);
        assertEquals("Running", interactor.receivedInputData.getName());
        assertEquals(true, interactor.receivedInputData.isCardio());
        assertEquals("5.0", interactor.receivedInputData.getDistance());
        assertEquals("30.0", interactor.receivedInputData.getDuration());
    }

    private static final class FakeAddExercisePerformedInputBoundary implements AddExercisePerformedInputBoundary {
        private boolean executeCalled;
        private AddExercisePerformedInputData receivedInputData;

        @Override
        public void execute(final AddExercisePerformedInputData addExercisePerformedInputData) {
            this.executeCalled = true;
            this.receivedInputData = addExercisePerformedInputData;
        }
    }
}
