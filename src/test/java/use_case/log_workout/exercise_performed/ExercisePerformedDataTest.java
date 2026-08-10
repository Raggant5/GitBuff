package use_case.log_workout.exercise_performed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import use_case.log_workout.StrengthDetailsData;

public class ExercisePerformedDataTest {

    @Test
    public void constructorStoresStrengthExerciseFields() {
        final StrengthDetailsData strengthDetailsData = new StrengthDetailsData(3, 10, 45.5);
        final ExercisePerformedData data = new ExercisePerformedData(9, "Bench Press", strengthDetailsData,
                20.0, null, false);

        assertEquals(9, data.getId());
        assertEquals("Bench Press", data.getExerciseName());
        assertEquals(3, data.getSets());
        assertEquals(10, data.getReps());
        assertEquals(45.5, data.getWeight(), 0.0001);
        assertEquals(20.0, data.getDurationMins(), 0.0001);
        assertNull(data.getDistanceKm());
        assertFalse(data.getIsCardio());
    }

    @Test
    public void constructorStoresCardioExerciseFields() {
        final StrengthDetailsData strengthDetailsData = new StrengthDetailsData(null, null, null);
        final ExercisePerformedData data = new ExercisePerformedData(1, "Running", strengthDetailsData,
                30.0, 5.0, true);

        assertEquals(5.0, data.getDistanceKm(), 0.0001);
        assertTrue(data.getIsCardio());
    }
}
