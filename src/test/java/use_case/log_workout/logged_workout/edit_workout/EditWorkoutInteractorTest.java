package use_case.log_workout.logged_workout.edit_workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entity.ExercisePerformed;
import entity.ExercisePerformedFactory;
import entity.LoggedWorkout;
import use_case.log_workout.StrengthDetailsInput;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;
import use_case.log_workout.exercise_performed.ExercisePerformedInputData;
import use_case.log_workout.logged_workout.get_workouts.ViewWorkoutDataAccessInterface;

public class EditWorkoutInteractorTest {

    @Test
    public void executeSavesNewExerciseAndUpdatesExistingExercise() {
        final LoggedWorkout existingWorkout = new LoggedWorkout("amir", LocalDate.of(2026, 8, 1));
        existingWorkout.setId(5);
        final FakeViewWorkoutDataAccessObject viewDataAccess = new FakeViewWorkoutDataAccessObject(existingWorkout);
        final FakeEditWorkoutDataAccessObject editDataAccess = new FakeEditWorkoutDataAccessObject();

        final ExercisePerformedInputData newExercise = new ExercisePerformedInputData(null, "Bench Press",
                new StrengthDetailsInput("3", "10", "45.0"), 20.0, null, false);
        final ExercisePerformedInputData existingExercise = new ExercisePerformedInputData(2, "Squats",
                new StrengthDetailsInput("4", "8", "60.0"), 15.0, null, false);
        final EditWorkoutInputData inputData = new EditWorkoutInputData(5,
                List.of(newExercise, existingExercise), List.of());

        final EditWorkoutOutputBoundary presenter = new EditWorkoutOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWorkoutOutputData outputData) {
                assertEquals(5, outputData.getId());
                assertEquals(LocalDate.of(2026, 8, 1), outputData.getDate());
                assertEquals(2, outputData.getExercises().size());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new EditWorkoutInteractor(presenter, editDataAccess, viewDataAccess,
                new ExercisePerformedFactory()).execute(inputData);

        assertTrue(editDataAccess.editedWorkout.getExercises().stream()
                .anyMatch(exercise -> exercise.getId() != null && exercise.getId() > 0
                        && "Bench Press".equals(exercise.getExerciseName())));
        assertTrue(editDataAccess.editedWorkout.getExercises().stream()
                .anyMatch(exercise -> exercise.getId() != null && exercise.getId() == 2));
    }

    @Test
    public void executeTreatsTempNegativeExerciseIdAsNotYetSaved() {
        final LoggedWorkout existingWorkout = new LoggedWorkout("amir", LocalDate.now());
        existingWorkout.setId(5);
        final FakeViewWorkoutDataAccessObject viewDataAccess = new FakeViewWorkoutDataAccessObject(existingWorkout);
        final FakeEditWorkoutDataAccessObject editDataAccess = new FakeEditWorkoutDataAccessObject();

        final ExercisePerformedInputData tempExercise = new ExercisePerformedInputData(-4, "Bench Press",
                new StrengthDetailsInput("3", "10", "45.0"), 20.0, null, false);
        final EditWorkoutInputData inputData = new EditWorkoutInputData(5, List.of(tempExercise), List.of());

        final EditWorkoutOutputBoundary presenter = new EditWorkoutOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWorkoutOutputData outputData) {
                // no-op
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new EditWorkoutInteractor(presenter, editDataAccess, viewDataAccess,
                new ExercisePerformedFactory()).execute(inputData);

        assertTrue(editDataAccess.editedWorkout.getExercises().stream()
                .anyMatch(exercise -> exercise.getId() != null && "Bench Press".equals(exercise.getExerciseName())));
    }

    @Test
    public void executeWithNonNumericStrengthDetailsDefaultsToNull() {
        final LoggedWorkout existingWorkout = new LoggedWorkout("amir", LocalDate.of(2026, 8, 1));
        existingWorkout.setId(5);
        final FakeViewWorkoutDataAccessObject viewDataAccess = new FakeViewWorkoutDataAccessObject(existingWorkout);
        final FakeEditWorkoutDataAccessObject editDataAccess = new FakeEditWorkoutDataAccessObject();

        final ExercisePerformedInputData badExercise = new ExercisePerformedInputData(null, "Bench Press",
                new StrengthDetailsInput("not-a-number", "not-a-number", "not-a-number"), 20.0, null, false);
        final EditWorkoutInputData inputData = new EditWorkoutInputData(5, List.of(badExercise), List.of());

        final EditWorkoutOutputBoundary presenter = new EditWorkoutOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWorkoutOutputData outputData) {
                assertEquals(null, outputData.getExercises().get(0).getSets());
                assertEquals(null, outputData.getExercises().get(0).getReps());
                assertEquals(null, outputData.getExercises().get(0).getWeight());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new EditWorkoutInteractor(presenter, editDataAccess, viewDataAccess,
                new ExercisePerformedFactory()).execute(inputData);
    }

    @Test
    public void executeOnlyDeletesExercisesWithRealPersistedIds() {
        final LoggedWorkout existingWorkout = new LoggedWorkout("amir", LocalDate.now());
        existingWorkout.setId(5);
        final FakeViewWorkoutDataAccessObject viewDataAccess = new FakeViewWorkoutDataAccessObject(existingWorkout);
        final FakeEditWorkoutDataAccessObject editDataAccess = new FakeEditWorkoutDataAccessObject();

        final EditWorkoutInputData inputData = new EditWorkoutInputData(5, List.of(),
                Arrays.asList(3, null, -1, -2));

        final EditWorkoutOutputBoundary presenter = new EditWorkoutOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWorkoutOutputData outputData) {
                // no-op
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new EditWorkoutInteractor(presenter, editDataAccess, viewDataAccess,
                new ExercisePerformedFactory()).execute(inputData);

        assertEquals(List.of(3), editDataAccess.deletedIds);
    }

    @Test
    public void executeWhenDataAccessThrowsPreparesFailView() {
        final FakeViewWorkoutDataAccessObject viewDataAccess =
                new FakeViewWorkoutDataAccessObject(new LoggedWorkout("amir", LocalDate.now()));
        final EditWorkoutDataAccessInterface failingDataAccess = (workout, exerciseIdsToDelete) -> {
            throw new DataAccessException("db unavailable");
        };
        final EditWorkoutInputData inputData = new EditWorkoutInputData(5, List.of(), List.of());
        final boolean[] failed = {false};

        final EditWorkoutOutputBoundary presenter = new EditWorkoutOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWorkoutOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new EditWorkoutInteractor(presenter, failingDataAccess, viewDataAccess,
                new ExercisePerformedFactory()).execute(inputData);
        assertTrue(failed[0]);
    }

    private static final class FakeViewWorkoutDataAccessObject implements ViewWorkoutDataAccessInterface {
        private final LoggedWorkout workout;

        private FakeViewWorkoutDataAccessObject(LoggedWorkout workout) {
            this.workout = workout;
        }

        @Override
        public List<LoggedWorkout> getWorkoutsForUser(String userId) {
            return List.of(workout);
        }

        @Override
        public List<ExercisePerformed> getExercisesForWorkout(int workoutId) {
            return workout.getExercises();
        }

        @Override
        public LoggedWorkout getWorkoutById(int workoutId) {
            return workout;
        }
    }

    private static final class FakeEditWorkoutDataAccessObject implements EditWorkoutDataAccessInterface {
        private final List<Integer> deletedIds = new ArrayList<>();
        private LoggedWorkout editedWorkout;

        @Override
        public LoggedWorkout editWorkout(LoggedWorkout workout, List<Integer> exerciseIdsToDelete) {
            for (Integer id : exerciseIdsToDelete) {
                if (id != null && id > 0) {
                    deletedIds.add(id);
                }
            }

            int nextId = 100;
            for (ExercisePerformed exercise : workout.getExercises()) {
                if (exercise.getId() == null) {
                    exercise.setId(nextId++);
                }
            }
            editedWorkout = workout;
            return workout;
        }
    }
}
