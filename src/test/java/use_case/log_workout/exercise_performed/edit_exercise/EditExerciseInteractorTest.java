package use_case.log_workout.exercise_performed.edit_exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.ExercisePerformedFactory;
import org.junit.jupiter.api.Test;
import use_case.log_workout.StrengthDetailsInput;
import use_case.log_workout.exercise_performed.ExerciseValidationErrors;

public class EditExerciseInteractorTest {

    @Test
    public void executeWithExistingIdSucceedsAndReturnsSameId() {
        final EditExerciseInputData inputData = new EditExerciseInputData(
                12, "Bench Press", new StrengthDetailsInput("3", "10", "45.5"), false, "", "20.0");

        final EditExerciseOutputBoundary presenter = new EditExerciseOutputBoundary() {
            @Override
            public void prepareSuccessView(EditExerciseOutputData outputData) {
                assertEquals(12, outputData.getId());
                assertEquals("Bench Press", outputData.getExerciseName());
                assertEquals(3, outputData.getSets());
                assertEquals(10, outputData.getReps());
                assertEquals(45.5, outputData.getWeight(), 0.0001);
                assertEquals(3, outputData.getStrengthDetailsData().getSets());
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                throw new AssertionError("Expected success view");
            }
        };

        new EditExerciseInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
    }

    @Test
    public void executeWithUnsavedTempIdSucceedsAndKeepsId() {
        final EditExerciseInputData inputData = new EditExerciseInputData(
                -1, "Running", new StrengthDetailsInput("", "", ""), true, "5.0", "30.0");

        final EditExerciseOutputBoundary presenter = new EditExerciseOutputBoundary() {
            @Override
            public void prepareSuccessView(EditExerciseOutputData outputData) {
                assertEquals(-1, outputData.getId());
                assertTrue(outputData.getIsCardio());
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                throw new AssertionError("Expected success view");
            }
        };

        new EditExerciseInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
    }

    @Test
    public void executeWithValidCardioExerciseSucceeds() {
        final EditExerciseInputData inputData = new EditExerciseInputData(
                4, "Running", new StrengthDetailsInput("", "", ""), true, "5.0", "30.0");

        final EditExerciseOutputBoundary presenter = new EditExerciseOutputBoundary() {
            @Override
            public void prepareSuccessView(EditExerciseOutputData outputData) {
                assertEquals(4, outputData.getId());
                assertTrue(outputData.getIsCardio());
                assertEquals(5.0, outputData.getDistanceKm(), 0.0001);
                assertEquals(30.0, outputData.getDurationMins(), 0.0001);
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                throw new AssertionError("Expected success view");
            }
        };

        new EditExerciseInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
    }

    @Test
    public void executeWithNullNameFails() {
        final EditExerciseInputData inputData = new EditExerciseInputData(
                12, null, new StrengthDetailsInput("3", "10", "45.5"), false, "", "20.0");
        final boolean[] failed = {false};

        final EditExerciseOutputBoundary presenter = new EditExerciseOutputBoundary() {
            @Override
            public void prepareSuccessView(EditExerciseOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getGeneralError().isEmpty());
            }
        };

        new EditExerciseInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNonNumericSetsFailsWithSetsError() {
        final EditExerciseInputData inputData = new EditExerciseInputData(
                12, "Bench Press", new StrengthDetailsInput("not-a-number", "10", "45.5"), false, "", "20.0");
        final boolean[] failed = {false};

        final EditExerciseOutputBoundary presenter = new EditExerciseOutputBoundary() {
            @Override
            public void prepareSuccessView(EditExerciseOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getSetsError().isEmpty());
            }
        };

        new EditExerciseInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNonPositiveSetsFailsWithSetsError() {
        final EditExerciseInputData inputData = new EditExerciseInputData(
                12, "Bench Press", new StrengthDetailsInput("0", "10", "45.5"), false, "", "20.0");
        final boolean[] failed = {false};

        final EditExerciseOutputBoundary presenter = new EditExerciseOutputBoundary() {
            @Override
            public void prepareSuccessView(EditExerciseOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getSetsError().isEmpty());
            }
        };

        new EditExerciseInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNonPositiveRepsFailsWithRepsError() {
        final EditExerciseInputData inputData = new EditExerciseInputData(
                12, "Bench Press", new StrengthDetailsInput("3", "0", "45.5"), false, "", "20.0");
        final boolean[] failed = {false};

        final EditExerciseOutputBoundary presenter = new EditExerciseOutputBoundary() {
            @Override
            public void prepareSuccessView(EditExerciseOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getRepsError().isEmpty());
            }
        };

        new EditExerciseInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNonPositiveDurationFailsWithDurationError() {
        final EditExerciseInputData inputData = new EditExerciseInputData(
                12, "Bench Press", new StrengthDetailsInput("3", "10", "45.5"), false, "", "0");
        final boolean[] failed = {false};

        final EditExerciseOutputBoundary presenter = new EditExerciseOutputBoundary() {
            @Override
            public void prepareSuccessView(EditExerciseOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getDurationError().isEmpty());
            }
        };

        new EditExerciseInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNonNumericWeightFailsWithWeightError() {
        final EditExerciseInputData inputData = new EditExerciseInputData(
                12, "Bench Press", new StrengthDetailsInput("3", "10", "not-a-number"), false, "", "20.0");
        final boolean[] failed = {false};

        final EditExerciseOutputBoundary presenter = new EditExerciseOutputBoundary() {
            @Override
            public void prepareSuccessView(EditExerciseOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getWeightError().isEmpty());
            }
        };

        new EditExerciseInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithInvalidDistanceForCardioFailsWithDistanceError() {
        final EditExerciseInputData inputData = new EditExerciseInputData(
                12, "Running", new StrengthDetailsInput("", "", ""), true, "not-a-number", "30.0");
        final boolean[] failed = {false};

        final EditExerciseOutputBoundary presenter = new EditExerciseOutputBoundary() {
            @Override
            public void prepareSuccessView(EditExerciseOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getDistanceError().isEmpty());
            }
        };

        new EditExerciseInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithBlankNameFails() {
        final EditExerciseInputData inputData = new EditExerciseInputData(
                12, "", new StrengthDetailsInput("3", "10", "45.5"), false, "", "20.0");
        final boolean[] failed = {false};

        final EditExerciseOutputBoundary presenter = new EditExerciseOutputBoundary() {
            @Override
            public void prepareSuccessView(EditExerciseOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getGeneralError().isEmpty());
            }
        };

        new EditExerciseInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNegativeWeightFailsWithWeightError() {
        final EditExerciseInputData inputData = new EditExerciseInputData(
                12, "Bench Press", new StrengthDetailsInput("3", "10", "-10"), false, "", "20.0");
        final boolean[] failed = {false};

        final EditExerciseOutputBoundary presenter = new EditExerciseOutputBoundary() {
            @Override
            public void prepareSuccessView(EditExerciseOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getWeightError().isEmpty());
            }
        };

        new EditExerciseInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }
}
