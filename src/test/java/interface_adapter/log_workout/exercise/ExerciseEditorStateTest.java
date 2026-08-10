package interface_adapter.log_workout.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Exercise Editor State.
 */
class ExerciseEditorStateTest {

    @Test
    void defaultsMatchExpectedInitialValues() {
        final ExerciseEditorState state = new ExerciseEditorState();

        assertNull(state.getEditingExercisePerformedId());
        assertEquals("", state.getExerciseName());
        assertEquals("", state.getSets());
        assertEquals("", state.getReps());
        assertEquals("", state.getWeight());
        assertEquals("1.0", state.getDurationMins());
        assertEquals("", state.getDistanceKm());
        assertFalse(state.getIsCardio());
        assertEquals("", state.getSetsError());
        assertEquals("", state.getRepsError());
        assertEquals("", state.getWeightError());
        assertEquals("", state.getDurationError());
        assertEquals("", state.getDistanceError());
        assertEquals("", state.getSubmitError());
    }

    @Test
    void settersUpdateEveryField() {
        final ExerciseEditorState state = new ExerciseEditorState();

        state.setEditingExercisePerformedId(3);
        state.setExerciseName("Bench Press");
        state.setSets("3");
        state.setReps("10");
        state.setWeight("50.0");
        state.setDurationMins("5.0");
        state.setDistanceKm("2.0");
        state.setIsCardio(true);
        state.setSetsError("Sets error");
        state.setRepsError("Reps error");
        state.setWeightError("Weight error");
        state.setDurationError("Duration error");
        state.setDistanceError("Distance error");
        state.setSubmitError("Submit error");

        assertEquals(3, state.getEditingExercisePerformedId());
        assertEquals("Bench Press", state.getExerciseName());
        assertEquals("3", state.getSets());
        assertEquals("10", state.getReps());
        assertEquals("50.0", state.getWeight());
        assertEquals("5.0", state.getDurationMins());
        assertEquals("2.0", state.getDistanceKm());
        assertTrue(state.getIsCardio());
        assertEquals("Sets error", state.getSetsError());
        assertEquals("Reps error", state.getRepsError());
        assertEquals("Weight error", state.getWeightError());
        assertEquals("Duration error", state.getDurationError());
        assertEquals("Distance error", state.getDistanceError());
        assertEquals("Submit error", state.getSubmitError());
    }

    @Test
    void resetRestoresDefaultsAfterFieldsAreChanged() {
        final ExerciseEditorState state = new ExerciseEditorState();
        state.setEditingExercisePerformedId(3);
        state.setExerciseName("Bench Press");
        state.setSets("3");
        state.setReps("10");
        state.setWeight("50.0");
        state.setDurationMins("5.0");
        state.setDistanceKm("2.0");
        state.setIsCardio(true);
        state.setSetsError("Sets error");
        state.setRepsError("Reps error");
        state.setWeightError("Weight error");
        state.setDurationError("Duration error");
        state.setDistanceError("Distance error");
        state.setSubmitError("Submit error");

        state.reset();

        assertNull(state.getEditingExercisePerformedId());
        assertEquals("", state.getExerciseName());
        assertEquals("", state.getSets());
        assertEquals("", state.getReps());
        assertEquals("", state.getWeight());
        assertEquals("1.0", state.getDurationMins());
        assertEquals("", state.getDistanceKm());
        assertFalse(state.getIsCardio());
        assertEquals("", state.getSetsError());
        assertEquals("", state.getRepsError());
        assertEquals("", state.getWeightError());
        assertEquals("", state.getDurationError());
        assertEquals("", state.getDistanceError());
        assertEquals("", state.getSubmitError());
    }
}
