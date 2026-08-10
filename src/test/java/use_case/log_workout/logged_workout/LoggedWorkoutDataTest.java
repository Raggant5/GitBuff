package use_case.log_workout.logged_workout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import entity.ExercisePerformed;
import entity.LoggedWorkout;

class LoggedWorkoutDataTest {

    private static final double DURATION_MINS = 30.0;

    @Test
    void fromConvertsWorkoutWithExercises() {
        final LoggedWorkout workout = new LoggedWorkout("aahir", LocalDate.of(2026, 1, 1));
        workout.setId(1);
        workout.getExercises().add(new ExercisePerformed("Push-Ups", null, DURATION_MINS, null, false));

        final LoggedWorkoutData data = LoggedWorkoutData.from(workout);

        assertEquals(1, data.getId());
        assertEquals(LocalDate.of(2026, 1, 1), data.getDate());
        assertEquals(1, data.getExercises().size());
        assertEquals("Push-Ups", data.getExercises().get(0).getExerciseName());
        assertEquals(DURATION_MINS, data.getExercises().get(0).getDurationMins());
    }
}
