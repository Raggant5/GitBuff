package use_case.log_workout.logged_workout.add_workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.ExercisePerformed;
import entity.LoggedWorkout;
import entity.LoggedWorkoutFactory;
import entity.ExercisePerformedFactory;
import use_case.log_workout.StrengthDetailsInput;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;
import use_case.log_workout.exercise_performed.ExercisePerformedInputData;

public class AddWorkoutInteractorTest {

    @Test
    public void executeWithExercisesSavesWorkoutAndAssignsGeneratedIds() {
        final FakeAddWorkoutDataAccessObject dataAccess = new FakeAddWorkoutDataAccessObject();
        final ExercisePerformedInputData exerciseData = new ExercisePerformedInputData(null, "Bench Press",
                new StrengthDetailsInput("3", "10", "45.0"), 20.0, null, false);
        assertEquals("3", exerciseData.getSets());
        assertEquals("10", exerciseData.getReps());
        assertEquals("45.0", exerciseData.getWeight());
        final AddWorkoutInputData inputData = new AddWorkoutInputData("amir", LocalDate.of(2026, 8, 6),
                List.of(exerciseData));

        final AddWorkoutOutputBoundary presenter = new AddWorkoutOutputBoundary() {
            @Override
            public void prepareSuccessView(AddWorkoutOutputData outputData) {
                assertTrue(outputData.getId() > 0);
                assertEquals(LocalDate.of(2026, 8, 6), outputData.getDate());
                assertEquals(1, outputData.getExercises().size());
                assertTrue(outputData.getExercises().get(0).getId() > 0);
                assertEquals("Bench Press", outputData.getExercises().get(0).getExerciseName());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new AddWorkoutInteractor(presenter, dataAccess, new LoggedWorkoutFactory(), new ExercisePerformedFactory())
                .execute(inputData);

        assertEquals(1, dataAccess.savedWorkouts.size());
        assertEquals(1, dataAccess.savedExercises.size());
    }

    @Test
    public void executeWithNonNumericStrengthDetailsDefaultsToNull() {
        final FakeAddWorkoutDataAccessObject dataAccess = new FakeAddWorkoutDataAccessObject();
        final ExercisePerformedInputData exerciseData = new ExercisePerformedInputData(null, "Bench Press",
                new StrengthDetailsInput("not-a-number", "not-a-number", "not-a-number"), 20.0, null, false);
        final AddWorkoutInputData inputData = new AddWorkoutInputData("amir", LocalDate.of(2026, 8, 6),
                List.of(exerciseData));

        final AddWorkoutOutputBoundary presenter = new AddWorkoutOutputBoundary() {
            @Override
            public void prepareSuccessView(AddWorkoutOutputData outputData) {
                assertEquals(null, outputData.getExercises().get(0).getSets());
                assertEquals(null, outputData.getExercises().get(0).getReps());
                assertEquals(null, outputData.getExercises().get(0).getWeight());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new AddWorkoutInteractor(presenter, dataAccess, new LoggedWorkoutFactory(), new ExercisePerformedFactory())
                .execute(inputData);
    }

    @Test
    public void executeWithNoExercisesSucceedsWithEmptyList() {
        final FakeAddWorkoutDataAccessObject dataAccess = new FakeAddWorkoutDataAccessObject();
        final AddWorkoutInputData inputData = new AddWorkoutInputData("amir", LocalDate.now(), List.of());
        final boolean[] succeeded = {false};

        final AddWorkoutOutputBoundary presenter = new AddWorkoutOutputBoundary() {
            @Override
            public void prepareSuccessView(AddWorkoutOutputData outputData) {
                succeeded[0] = true;
                assertTrue(outputData.getExercises().isEmpty());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new AddWorkoutInteractor(presenter, dataAccess, new LoggedWorkoutFactory(), new ExercisePerformedFactory())
                .execute(inputData);
        assertTrue(succeeded[0]);
    }

    @Test
    public void executeWhenDataAccessThrowsPreparesFailView() {
        final AddWorkoutDataAccessInterface failingDataAccess = new AddWorkoutDataAccessInterface() {
            @Override
            public int saveWorkout(LoggedWorkout workout) {
                throw new DataAccessException("db unavailable");
            }

            @Override
            public int saveExercisePerformed(ExercisePerformed exercisePerformed) {
                throw new DataAccessException("db unavailable");
            }
        };
        final AddWorkoutInputData inputData = new AddWorkoutInputData("amir", LocalDate.now(), List.of());
        final boolean[] failed = {false};

        final AddWorkoutOutputBoundary presenter = new AddWorkoutOutputBoundary() {
            @Override
            public void prepareSuccessView(AddWorkoutOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new AddWorkoutInteractor(presenter, failingDataAccess, new LoggedWorkoutFactory(),
                new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    private static final class FakeAddWorkoutDataAccessObject implements AddWorkoutDataAccessInterface {
        private final Map<Integer, LoggedWorkout> savedWorkouts = new HashMap<>();
        private final List<ExercisePerformed> savedExercises = new ArrayList<>();
        private int nextWorkoutId = 1;
        private int nextExerciseId = 1;

        @Override
        public int saveWorkout(LoggedWorkout workout) {
            final int id = nextWorkoutId++;
            workout.setId(id);
            savedWorkouts.put(id, workout);
            return id;
        }

        @Override
        public int saveExercisePerformed(ExercisePerformed exercisePerformed) {
            final int id = nextExerciseId++;
            exercisePerformed.setId(id);
            savedExercises.add(exercisePerformed);
            return id;
        }
    }
}
