package interface_adapter.log_workout.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.exercise_performed.ExercisePerformedData;
import use_case.log_workout.logged_workout.LoggedWorkoutData;
import use_case.log_workout.logged_workout.get_workouts.GetWorkoutsOutputData;

/**
 * Unit tests for the Get Workouts Presenter.
 */
class GetWorkoutsPresenterTest {

    @Test
    void prepareSuccessViewMapsWorkoutsAndClearsError() {
        final ViewWorkoutsViewModel viewWorkoutsViewModel = new ViewWorkoutsViewModel();
        viewWorkoutsViewModel.getState().setError("stale error");
        final GetWorkoutsPresenter presenter = new GetWorkoutsPresenter(viewWorkoutsViewModel);

        final ExercisePerformedData exercise = new ExercisePerformedData(1, "Deadlift",
                new StrengthDetailsData(5, 5, 140.0), 0, null, false);
        final LoggedWorkoutData firstWorkout = new LoggedWorkoutData(1, LocalDate.of(2024, 1, 1),
                List.of(exercise));
        final LoggedWorkoutData secondWorkout = new LoggedWorkoutData(2, LocalDate.of(2024, 2, 1), List.of());
        final GetWorkoutsOutputData outputData = new GetWorkoutsOutputData(List.of(firstWorkout, secondWorkout));

        presenter.prepareSuccessView(outputData);

        final ViewWorkoutsState state = viewWorkoutsViewModel.getState();
        assertEquals("", state.getError());
        assertEquals(2, state.getWorkouts().size());
        assertEquals(1, state.getWorkouts().get(0).getId());
        assertEquals(1, state.getWorkouts().get(0).getExercises().size());
        assertEquals("Deadlift", state.getWorkouts().get(0).getExercises().get(0).getExerciseName());
        assertEquals("5", state.getWorkouts().get(0).getExercises().get(0).getSets());
        assertEquals(2, state.getWorkouts().get(1).getId());
        assertTrue(state.getWorkouts().get(1).getExercises().isEmpty());
    }

    @Test
    void prepareFailViewSetsError() {
        final ViewWorkoutsViewModel viewWorkoutsViewModel = new ViewWorkoutsViewModel();
        final GetWorkoutsPresenter presenter = new GetWorkoutsPresenter(viewWorkoutsViewModel);

        presenter.prepareFailView("Could not load workouts.");

        assertEquals("Could not load workouts.", viewWorkoutsViewModel.getState().getError());
        assertTrue(viewWorkoutsViewModel.getState().getWorkouts().isEmpty());
    }
}
