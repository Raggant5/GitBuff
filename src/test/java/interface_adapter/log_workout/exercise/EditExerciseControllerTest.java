package interface_adapter.log_workout.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseInputBoundary;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseInputData;

/**
 * Unit tests for the Edit Exercise Controller.
 */
class EditExerciseControllerTest {

    @Test
    void executeBuildsInputDataAndDelegatesToInteractor() {
        final FakeEditExerciseInputBoundary interactor = new FakeEditExerciseInputBoundary();
        final EditExerciseController controller = new EditExerciseController(interactor);
        final StrengthDetailsDisplayData strengthDetailsDisplayData =
                new StrengthDetailsDisplayData("4", "8", "60.0");

        controller.execute(3, "Squat", strengthDetailsDisplayData, false, "", "10.0");

        assertTrue(interactor.executeCalled);
        assertEquals(3, interactor.receivedInputData.getId());
        assertEquals("Squat", interactor.receivedInputData.getExerciseName());
        assertEquals("4", interactor.receivedInputData.getSets());
        assertEquals("8", interactor.receivedInputData.getReps());
        assertEquals("60.0", interactor.receivedInputData.getWeight());
        assertEquals(false, interactor.receivedInputData.isCardio());
        assertEquals("", interactor.receivedInputData.getDistance());
        assertEquals("10.0", interactor.receivedInputData.getDuration());
    }

    @Test
    void executeWithCardioExercise() {
        final FakeEditExerciseInputBoundary interactor = new FakeEditExerciseInputBoundary();
        final EditExerciseController controller = new EditExerciseController(interactor);
        final StrengthDetailsDisplayData strengthDetailsDisplayData =
                new StrengthDetailsDisplayData("", "", "");

        controller.execute(9, "Cycling", strengthDetailsDisplayData, true, "12.5", "45.0");

        assertTrue(interactor.executeCalled);
        assertEquals(9, interactor.receivedInputData.getId());
        assertEquals(true, interactor.receivedInputData.isCardio());
        assertEquals("12.5", interactor.receivedInputData.getDistance());
        assertEquals("45.0", interactor.receivedInputData.getDuration());
    }

    private static final class FakeEditExerciseInputBoundary implements EditExerciseInputBoundary {
        private boolean executeCalled;
        private EditExerciseInputData receivedInputData;

        @Override
        public void execute(final EditExerciseInputData inputData) {
            this.executeCalled = true;
            this.receivedInputData = inputData;
        }
    }
}
