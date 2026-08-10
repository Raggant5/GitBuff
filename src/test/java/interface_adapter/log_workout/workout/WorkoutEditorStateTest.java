package interface_adapter.log_workout.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import interface_adapter.log_workout.exercise.StrengthDetailsDisplayData;

/**
 * Unit tests for the Workout Editor State.
 */
class WorkoutEditorStateTest {

    private static ExercisePerformedDisplayData exercise(Integer id, String name) {
        return new ExercisePerformedDisplayData(id, name,
                new StrengthDetailsDisplayData("3", "10", "50"), 0, null, false);
    }

    @Test
    void addExerciseAppendsToExercisesForWorkout() {
        final WorkoutEditorState state = new WorkoutEditorState();

        state.addExercise(exercise(1, "Bench Press"));

        assertEquals(1, state.getExercisesForWorkout().size());
        assertEquals("Bench Press", state.getExercisesForWorkout().get(0).getExerciseName());
    }

    @Test
    void removeExerciseByIdRemovesMatchingExercise() {
        final WorkoutEditorState state = new WorkoutEditorState();
        state.addExercise(exercise(1, "Bench Press"));
        state.addExercise(exercise(2, "Squat"));

        state.removeExerciseById(1);

        assertEquals(1, state.getExercisesForWorkout().size());
        assertEquals("Squat", state.getExercisesForWorkout().get(0).getExerciseName());
    }

    @Test
    void replaceExerciseReplacesMatchingId() {
        final WorkoutEditorState state = new WorkoutEditorState();
        state.addExercise(exercise(1, "Bench Press"));
        state.addExercise(exercise(2, "Squat"));

        state.replaceExercise(exercise(1, "Incline Bench Press"));

        assertEquals(2, state.getExercisesForWorkout().size());
        assertEquals("Incline Bench Press", state.getExercisesForWorkout().get(0).getExerciseName());
        assertEquals("Squat", state.getExercisesForWorkout().get(1).getExerciseName());
    }

    @Test
    void replaceExerciseWithUnknownIdLeavesListUnchanged() {
        final WorkoutEditorState state = new WorkoutEditorState();
        state.addExercise(exercise(1, "Bench Press"));

        state.replaceExercise(exercise(99, "Deadlift"));

        assertEquals(1, state.getExercisesForWorkout().size());
        assertEquals("Bench Press", state.getExercisesForWorkout().get(0).getExerciseName());
    }

    @Test
    void addExerciseToBeDeletedTracksIdInDeleteStage() {
        final WorkoutEditorState state = new WorkoutEditorState();

        state.addExerciseToBeDeleted(5);

        assertEquals(1, state.getExercisesDeleteStage().size());
        assertEquals(5, state.getExercisesDeleteStage().get(0));
    }

    @Test
    void resetRestoresDefaultValues() {
        final WorkoutEditorState state = new WorkoutEditorState();
        state.setEditingWorkoutId(7);
        state.addExercise(exercise(1, "Bench Press"));
        state.addExerciseToBeDeleted(1);
        state.setErrorMessage("bad input");
        state.setShowExerciseEditor(true);
        state.setNextTempId(3);

        state.reset();

        assertNull(state.getEditingWorkoutId());
        assertNull(state.getDate());
        assertTrue(state.getExercisesForWorkout().isEmpty());
        assertTrue(state.getExercisesDeleteStage().isEmpty());
        assertEquals("", state.getErrorMessage());
        assertFalse(state.getShowExerciseEditor());
        assertEquals(-1, state.getNextTempId());
    }
}
