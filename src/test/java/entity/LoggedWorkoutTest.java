package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

public class LoggedWorkoutTest {

    @Test
    public void constructorStoresUserIdAndDateWithEmptyExercises() {
        final LocalDate date = LocalDate.of(2026, 8, 6);
        final LoggedWorkout workout = new LoggedWorkout("amir", date);

        assertEquals("amir", workout.getUserId());
        assertEquals(date, workout.getDate());
        assertTrue(workout.getExercises().isEmpty());
        assertNull(workout.getId());
    }

    @Test
    public void addExerciseAppendsToTheList() {
        final LoggedWorkout workout = new LoggedWorkout("amir", LocalDate.now());
        final ExercisePerformed exercise = new ExercisePerformed("Deadlift",
                new StrengthDetails(3, 5, 100.0), 25.0, null, false);

        workout.addExercise(exercise);

        assertEquals(1, workout.getExercises().size());
        assertEquals("Deadlift", workout.getExercises().get(0).getExerciseName());
    }

    @Test
    public void setExercisesReplacesEntireList() {
        final LoggedWorkout workout = new LoggedWorkout("amir", LocalDate.now());
        final ExercisePerformed first = new ExercisePerformed("Bench Press",
                new StrengthDetails(3, 10, 45.0), 20.0, null, false);
        workout.setExercises(List.of(first));
        assertEquals(1, workout.getExercises().size());

        final ExercisePerformed second = new ExercisePerformed("Running",
                new StrengthDetails(null, null, null), 30.0, 5.0, true);
        workout.setExercises(List.of(second));

        assertEquals(1, workout.getExercises().size());
        assertEquals("Running", workout.getExercises().get(0).getExerciseName());
    }

    @Test
    public void removeExerciseRemovesMatchingEntry() {
        final LoggedWorkout workout = new LoggedWorkout("amir", LocalDate.now());
        final ExercisePerformed exercise = new ExercisePerformed("Squats",
                new StrengthDetails(4, 8, 60.0), 15.0, null, false);
        workout.setExercises(List.of(exercise));

        workout.removeExercise(workout.getExercises().get(0));

        assertTrue(workout.getExercises().isEmpty());
    }

    @Test
    public void idAndDateAreSettable() {
        final LoggedWorkout workout = new LoggedWorkout("amir", LocalDate.of(2026, 1, 1));

        workout.setId(9);
        final LocalDate updated = LocalDate.of(2026, 2, 2);
        workout.setDate(updated);

        assertEquals(9, workout.getId());
        assertEquals(updated, workout.getDate());
    }
}
