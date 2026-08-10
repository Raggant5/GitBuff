package use_case.log_workout.logged_workout.prepare_edit_workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;
import entity.StrengthDetails;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;
import use_case.log_workout.logged_workout.get_workouts.ViewWorkoutDataAccessInterface;

public class PrepareEditWorkoutInteractorTest {

    @Test
    public void executeFetchesWorkoutByIdAndMapsExercisesToData() {
        final LoggedWorkout workout = new LoggedWorkout("amir", LocalDate.of(2026, 8, 6));
        workout.setId(5);
        final ExercisePerformed exercise = new ExercisePerformed("Bench Press",
                new StrengthDetails(3, 10, 45.0), 20.0, null, false);
        exercise.setId(11);
        workout.setExercises(List.of(exercise));

        final ViewWorkoutDataAccessInterface dataAccess = new ViewWorkoutDataAccessInterface() {
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
                assertEquals(5, workoutId);
                return workout;
            }
        };

        final PrepareEditWorkoutOutputData[] captured = new PrepareEditWorkoutOutputData[1];
        final PrepareEditWorkoutOutputBoundary presenter = new PrepareEditWorkoutOutputBoundary() {
            @Override
            public void prepareSuccessView(PrepareEditWorkoutOutputData outputData) {
                captured[0] = outputData;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new PrepareEditWorkoutInteractor(presenter, dataAccess).execute(new PrepareEditWorkoutInputData(5));

        assertEquals(5, captured[0].getWorkoutId());
        assertEquals(LocalDate.of(2026, 8, 6), captured[0].getDate());
        assertEquals(1, captured[0].getExercises().size());
        assertEquals(11, captured[0].getExercises().get(0).getId());
        assertEquals("Bench Press", captured[0].getExercises().get(0).getExerciseName());
    }

    @Test
    public void executeWhenDataAccessThrowsPreparesFailView() {
        final ViewWorkoutDataAccessInterface failingDataAccess = new ViewWorkoutDataAccessInterface() {
            @Override
            public List<LoggedWorkout> getWorkoutsForUser(String userId) {
                return List.of();
            }

            @Override
            public List<ExercisePerformed> getExercisesForWorkout(int workoutId) {
                return List.of();
            }

            @Override
            public LoggedWorkout getWorkoutById(int workoutId) {
                throw new DataAccessException("db unavailable");
            }
        };
        final boolean[] failed = {false};
        final PrepareEditWorkoutOutputBoundary presenter = new PrepareEditWorkoutOutputBoundary() {
            @Override
            public void prepareSuccessView(PrepareEditWorkoutOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new PrepareEditWorkoutInteractor(presenter, failingDataAccess).execute(new PrepareEditWorkoutInputData(5));

        assertTrue(failed[0]);
    }
}
