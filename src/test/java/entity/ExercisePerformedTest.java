package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ExercisePerformedTest {

    @Test
    public void constructorStoresStrengthExerciseFields() {
        final StrengthDetails details = new StrengthDetails(3, 10, 45.5);
        final ExercisePerformed exercise = new ExercisePerformed("Bench Press", details, 20.0, null, false);

        assertEquals(details, exercise.getStrengthDetails());
        assertEquals("Bench Press", exercise.getExerciseName());
        assertEquals(3, exercise.getSets());
        assertEquals(10, exercise.getReps());
        assertEquals(45.5, exercise.getWeight(), 0.0001);
        assertEquals(20.0, exercise.getDurationMins(), 0.0001);
        assertNull(exercise.getDistanceKm());
        assertFalse(exercise.getIsCardio());
    }

    @Test
    public void constructorStoresCardioExerciseFields() {
        final StrengthDetails details = new StrengthDetails(null, null, null);
        final ExercisePerformed exercise = new ExercisePerformed("Running", details, 30.0, 5.0, true);

        assertNull(exercise.getSets());
        assertNull(exercise.getReps());
        assertNull(exercise.getWeight());
        assertEquals(5.0, exercise.getDistanceKm(), 0.0001);
        assertTrue(exercise.getIsCardio());
    }

    @Test
    public void newExerciseHasNoIdOrWorkoutIdUntilAssigned() {
        final ExercisePerformed exercise = new ExercisePerformed("Push-ups",
                new StrengthDetails(3, 15, 0.0), 10.0, null, false);

        assertNull(exercise.getId());
        assertNull(exercise.getWorkoutId());

        exercise.setId(42);
        exercise.setWorkoutId(7);

        assertEquals(42, exercise.getId());
        assertEquals(7, exercise.getWorkoutId());
    }

    @Test
    public void settersUpdateFields() {
        final ExercisePerformed exercise = new ExercisePerformed("Squats",
                new StrengthDetails(4, 8, 60.0), 15.0, null, false);

        exercise.setExerciseName("Front Squats");
        exercise.setDurationMins(18.0);
        exercise.setDistanceKm(1.5);
        exercise.setIsCardio(true);

        assertEquals("Front Squats", exercise.getExerciseName());
        assertEquals(18.0, exercise.getDurationMins(), 0.0001);
        assertEquals(1.5, exercise.getDistanceKm(), 0.0001);
        assertTrue(exercise.getIsCardio());
    }
}
