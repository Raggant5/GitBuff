package interface_adapter.log_workout.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import interface_adapter.log_workout.exercise.StrengthDetailsDisplayData;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutInputBoundary;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutInputData;

/**
 * Unit tests for the Edit Workout Controller.
 */
class EditWorkoutControllerTest {

    @Test
    void executeMapsExercisesAndDeletionsAndDelegatesToInteractor() {
        final FakeEditWorkoutInteractor interactor = new FakeEditWorkoutInteractor();
        final EditWorkoutController controller = new EditWorkoutController(interactor);

        final ExercisePerformedDisplayData strength = new ExercisePerformedDisplayData(1, "Squat",
                new StrengthDetailsDisplayData("4", "8", "100"), 0, null, false);
        final ExercisePerformedDisplayData cardio = new ExercisePerformedDisplayData(2, "Cycling",
                new StrengthDetailsDisplayData("", "", ""), 20, 10.0, true);

        controller.execute(9, Arrays.asList(strength, cardio), List.of(3, 4));

        assertTrue(interactor.executeCalled);
        final EditWorkoutInputData receivedData = interactor.receivedInputData;
        assertEquals(9, receivedData.getWorkoutId());
        assertEquals(2, receivedData.getExercises().size());
        assertEquals("Squat", receivedData.getExercises().get(0).getExerciseName());
        assertEquals("4", receivedData.getExercises().get(0).getSets());
        assertEquals("Cycling", receivedData.getExercises().get(1).getExerciseName());
        assertTrue(receivedData.getExercises().get(1).getIsCardio());
        assertEquals(List.of(3, 4), receivedData.getExerciseIdsToDelete());
    }

    private static final class FakeEditWorkoutInteractor implements EditWorkoutInputBoundary {
        private boolean executeCalled;
        private EditWorkoutInputData receivedInputData;

        @Override
        public void execute(EditWorkoutInputData inputData) {
            this.executeCalled = true;
            this.receivedInputData = inputData;
        }
    }
}
