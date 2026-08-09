package use_case.log_workout.exercise_performed.create_exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.ExercisePerformedFactory;
import org.junit.jupiter.api.Test;
import use_case.log_workout.StrengthDetailsInput;
import use_case.log_workout.exercise_performed.ExerciseValidationErrors;

public class AddExercisePerformedInteractorTest {

    @Test
    public void executeWithValidStrengthExerciseSucceeds() {
        final AddExercisePerformedInputData inputData = new AddExercisePerformedInputData(
                "Bench Press", new StrengthDetailsInput("3", "10", "45.5"), false, "", "20.0");

        final AddExercisePerformedOutputBoundary presenter = new AddExercisePerformedOutputBoundary() {
            @Override
            public void prepareSuccessView(AddExercisePerformedOutputData outputData) {
                assertEquals("Bench Press", outputData.getExerciseName());
                assertEquals(3, outputData.getSets());
                assertEquals(10, outputData.getReps());
                assertEquals(45.5, outputData.getWeight(), 0.0001);
                assertEquals(20.0, outputData.getDurationMins(), 0.0001);
                assertFalse(outputData.getIsCardio());
                assertNull(outputData.getId());
                assertEquals(3, outputData.getStrengthDetailsData().getSets());
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                throw new AssertionError("Expected success view");
            }
        };

        new AddExercisePerformedInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
    }

    @Test
    public void executeWithValidCardioExerciseSucceeds() {
        final AddExercisePerformedInputData inputData = new AddExercisePerformedInputData(
                "Running", new StrengthDetailsInput("", "", ""), true, "5.0", "30.0");

        final AddExercisePerformedOutputBoundary presenter = new AddExercisePerformedOutputBoundary() {
            @Override
            public void prepareSuccessView(AddExercisePerformedOutputData outputData) {
                assertEquals("Running", outputData.getExerciseName());
                assertEquals(5.0, outputData.getDistanceKm(), 0.0001);
                assertTrue(outputData.getIsCardio());
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                throw new AssertionError("Expected success view");
            }
        };

        new AddExercisePerformedInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
    }

    @Test
    public void executeWithBlankNameFails() {
        final AddExercisePerformedInputData inputData = new AddExercisePerformedInputData(
                "", new StrengthDetailsInput("3", "10", "45.5"), false, "", "20.0");
        final boolean[] failed = {false};

        final AddExercisePerformedOutputBoundary presenter = new AddExercisePerformedOutputBoundary() {
            @Override
            public void prepareSuccessView(AddExercisePerformedOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getGeneralError().isEmpty());
            }
        };

        new AddExercisePerformedInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNullNameFails() {
        final AddExercisePerformedInputData inputData = new AddExercisePerformedInputData(
                null, new StrengthDetailsInput("3", "10", "45.5"), false, "", "20.0");
        final boolean[] failed = {false};

        final AddExercisePerformedOutputBoundary presenter = new AddExercisePerformedOutputBoundary() {
            @Override
            public void prepareSuccessView(AddExercisePerformedOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getGeneralError().isEmpty());
            }
        };

        new AddExercisePerformedInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNonPositiveSetsFailsWithSetsError() {
        final AddExercisePerformedInputData inputData = new AddExercisePerformedInputData(
                "Bench Press", new StrengthDetailsInput("0", "10", "45.5"), false, "", "20.0");
        final boolean[] failed = {false};

        final AddExercisePerformedOutputBoundary presenter = new AddExercisePerformedOutputBoundary() {
            @Override
            public void prepareSuccessView(AddExercisePerformedOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getSetsError().isEmpty());
            }
        };

        new AddExercisePerformedInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithInvalidDistanceForCardioFailsWithDistanceError() {
        final AddExercisePerformedInputData inputData = new AddExercisePerformedInputData(
                "Running", new StrengthDetailsInput("", "", ""), true, "not-a-number", "30.0");
        final boolean[] failed = {false};

        final AddExercisePerformedOutputBoundary presenter = new AddExercisePerformedOutputBoundary() {
            @Override
            public void prepareSuccessView(AddExercisePerformedOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getDistanceError().isEmpty());
            }
        };

        new AddExercisePerformedInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNonPositiveRepsFailsWithRepsError() {
        final AddExercisePerformedInputData inputData = new AddExercisePerformedInputData(
                "Bench Press", new StrengthDetailsInput("3", "0", "45.5"), false, "", "20.0");
        final boolean[] failed = {false};

        final AddExercisePerformedOutputBoundary presenter = new AddExercisePerformedOutputBoundary() {
            @Override
            public void prepareSuccessView(AddExercisePerformedOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getRepsError().isEmpty());
            }
        };

        new AddExercisePerformedInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNegativeWeightFailsWithWeightError() {
        final AddExercisePerformedInputData inputData = new AddExercisePerformedInputData(
                "Bench Press", new StrengthDetailsInput("3", "10", "-45.5"), false, "", "20.0");
        final boolean[] failed = {false};

        final AddExercisePerformedOutputBoundary presenter = new AddExercisePerformedOutputBoundary() {
            @Override
            public void prepareSuccessView(AddExercisePerformedOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getWeightError().isEmpty());
            }
        };

        new AddExercisePerformedInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNonNumericSetsFailsWithSetsError() {
        final AddExercisePerformedInputData inputData = new AddExercisePerformedInputData(
                "Bench Press", new StrengthDetailsInput("not-a-number", "10", "45.5"), false, "", "20.0");
        final boolean[] failed = {false};

        final AddExercisePerformedOutputBoundary presenter = new AddExercisePerformedOutputBoundary() {
            @Override
            public void prepareSuccessView(AddExercisePerformedOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getSetsError().isEmpty());
            }
        };

        new AddExercisePerformedInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNonNumericWeightFailsWithWeightError() {
        final AddExercisePerformedInputData inputData = new AddExercisePerformedInputData(
                "Bench Press", new StrengthDetailsInput("3", "10", "not-a-number"), false, "", "20.0");
        final boolean[] failed = {false};

        final AddExercisePerformedOutputBoundary presenter = new AddExercisePerformedOutputBoundary() {
            @Override
            public void prepareSuccessView(AddExercisePerformedOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getWeightError().isEmpty());
            }
        };

        new AddExercisePerformedInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    @Test
    public void executeWithNonPositiveDurationFailsWithDurationError() {
        final AddExercisePerformedInputData inputData = new AddExercisePerformedInputData(
                "Bench Press", new StrengthDetailsInput("3", "10", "45.5"), false, "", "0");
        final boolean[] failed = {false};

        final AddExercisePerformedOutputBoundary presenter = new AddExercisePerformedOutputBoundary() {
            @Override
            public void prepareSuccessView(AddExercisePerformedOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(ExerciseValidationErrors errors) {
                failed[0] = true;
                assertFalse(errors.getDurationError().isEmpty());
            }
        };

        new AddExercisePerformedInteractor(presenter, new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }
}
