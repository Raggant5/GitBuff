package use_case.log_workout.exercise_performed.prepare_edit_exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import use_case.log_workout.StrengthDetailsInput;
import org.junit.jupiter.api.Test;

public class PrepareEditExerciseInteractorTest {

    @Test
    public void executePassesInputFieldsThroughUnchanged() {
        final PrepareEditExerciseInputData inputData = new PrepareEditExerciseInputData(
                7, "Bench Press", new StrengthDetailsInput("3", "10", "45.5"), 20.0, null, false);
        final PrepareEditExerciseOutputData[] captured = new PrepareEditExerciseOutputData[1];

        final PrepareEditExerciseOutputBoundary presenter = outputData -> captured[0] = outputData;

        new PrepareEditExerciseInteractor(presenter).execute(inputData);

        assertEquals(7, captured[0].getId());
        assertEquals("Bench Press", captured[0].getExerciseName());
        assertEquals(3, captured[0].getStrengthDetailsData().getSets());
        assertEquals(10, captured[0].getStrengthDetailsData().getReps());
        assertEquals(45.5, captured[0].getStrengthDetailsData().getWeight(), 0.0001);
        assertEquals(20.0, captured[0].getDurationMins(), 0.0001);
        assertEquals(null, captured[0].getDistanceKm());
        assertEquals(false, captured[0].getIsCardio());
    }

    @Test
    public void executeWithNonNumericStrengthDetailsDefaultsToNull() {
        final PrepareEditExerciseInputData inputData = new PrepareEditExerciseInputData(
                7, "Bench Press", new StrengthDetailsInput("not-a-number", "not-a-number", "not-a-number"),
                20.0, null, false);
        final PrepareEditExerciseOutputData[] captured = new PrepareEditExerciseOutputData[1];

        final PrepareEditExerciseOutputBoundary presenter = outputData -> captured[0] = outputData;

        new PrepareEditExerciseInteractor(presenter).execute(inputData);

        assertNull(captured[0].getStrengthDetailsData().getSets());
        assertNull(captured[0].getStrengthDetailsData().getReps());
        assertNull(captured[0].getStrengthDetailsData().getWeight());
    }
}
