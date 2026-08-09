package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ExercisePerformedFactoryTest {

    @Test
    public void createReturnsExerciseWithGivenFieldsAndNoId() {
        final ExercisePerformedFactory factory = new ExercisePerformedFactory();
        final StrengthDetails details = new StrengthDetails(3, 10, 45.0);

        final ExercisePerformed exercise = factory.create("Bench Press", details, 20.0, null, false);

        assertEquals("Bench Press", exercise.getExerciseName());
        assertEquals(3, exercise.getSets());
        assertEquals(20.0, exercise.getDurationMins(), 0.0001);
        assertNull(exercise.getId());
    }
}
