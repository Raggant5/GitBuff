package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class LoggedWorkoutFactoryTest {

    @Test
    public void createReturnsWorkoutWithGivenFieldsAndNoId() {
        final LoggedWorkoutFactory factory = new LoggedWorkoutFactory();
        final LocalDate date = LocalDate.of(2026, 8, 6);

        final LoggedWorkout workout = factory.create("amir", date);

        assertEquals("amir", workout.getUserId());
        assertEquals(date, workout.getDate());
        assertTrue(workout.getExercises().isEmpty());
        assertNull(workout.getId());
    }
}
