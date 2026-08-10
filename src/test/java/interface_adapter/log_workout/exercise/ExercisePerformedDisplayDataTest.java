package interface_adapter.log_workout.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Exercise Performed Display Data.
 */
class ExercisePerformedDisplayDataTest {

    @Test
    void gettersReturnConstructedValuesAndDelegateStrengthDetails() {
        final StrengthDetailsDisplayData strengthDetailsDisplayData =
                new StrengthDetailsDisplayData("3", "10", "50.0");
        final ExercisePerformedDisplayData data = new ExercisePerformedDisplayData(
                5, "Bench Press", strengthDetailsDisplayData, 12.5, 2.0, true);

        assertEquals(5, data.getId());
        assertEquals("Bench Press", data.getExerciseName());
        assertEquals(strengthDetailsDisplayData, data.getStrengthDetailsDisplayData());
        assertEquals("3", data.getSets());
        assertEquals("10", data.getReps());
        assertEquals("50.0", data.getWeight());
        assertEquals(12.5, data.getDurationMins());
        assertEquals(2.0, data.getDistanceKm());
        assertTrue(data.getIsCardio());
    }

    @Test
    void gettersHandleNonCardioValues() {
        final StrengthDetailsDisplayData strengthDetailsDisplayData =
                new StrengthDetailsDisplayData("", "", "");
        final ExercisePerformedDisplayData data = new ExercisePerformedDisplayData(
                null, "Plank", strengthDetailsDisplayData, 1.0, null, false);

        assertEquals(null, data.getId());
        assertEquals(null, data.getDistanceKm());
        assertFalse(data.getIsCardio());
    }
}
