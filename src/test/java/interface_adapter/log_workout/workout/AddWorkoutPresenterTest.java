package interface_adapter.log_workout.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import interface_adapter.MainViewManagerModel;
import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.exercise_performed.ExercisePerformedData;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutOutputData;

/**
 * Unit tests for the Add Workout Presenter.
 */
class AddWorkoutPresenterTest {

    @Test
    void prepareSuccessViewResetsEditorAddsWorkoutAndSwitchesView() {
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        workoutEditorViewModel.getState().setNextTempId(5);
        workoutEditorViewModel.getState().setErrorMessage("stale error");
        final ViewWorkoutsViewModel viewWorkoutsViewModel = new ViewWorkoutsViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
        final AddWorkoutPresenter presenter = new AddWorkoutPresenter(workoutEditorViewModel,
                viewWorkoutsViewModel, mainViewManagerModel);

        final List<ExercisePerformedData> exercises = List.of(
                new ExercisePerformedData(1, "Bench Press", new StrengthDetailsData(3, 10, 60.0),
                        0, null, false));
        final AddWorkoutOutputData outputData = new AddWorkoutOutputData(7, LocalDate.of(2024, 1, 1), exercises);

        presenter.prepareSuccessView(outputData);

        assertEquals(-1, workoutEditorViewModel.getState().getNextTempId());
        assertEquals("", workoutEditorViewModel.getState().getErrorMessage());

        final ViewWorkoutsState workoutsState = viewWorkoutsViewModel.getState();
        assertEquals(1, workoutsState.getWorkouts().size());
        final LoggedWorkoutDisplayData workout = workoutsState.getWorkouts().get(0);
        assertEquals(7, workout.getId());
        assertEquals(LocalDate.of(2024, 1, 1), workout.getDate());
        assertEquals(1, workout.getExercises().size());
        assertEquals("Bench Press", workout.getExercises().get(0).getExerciseName());
        assertEquals("3", workout.getExercises().get(0).getSets());

        assertEquals("view workouts", mainViewManagerModel.getState());
    }

    @Test
    void prepareFailViewSetsErrorMessageOnEditorState() {
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        final ViewWorkoutsViewModel viewWorkoutsViewModel = new ViewWorkoutsViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
        final AddWorkoutPresenter presenter = new AddWorkoutPresenter(workoutEditorViewModel,
                viewWorkoutsViewModel, mainViewManagerModel);

        presenter.prepareFailView("Something went wrong.");

        assertEquals("Something went wrong.", workoutEditorViewModel.getState().getErrorMessage());
        assertTrue(viewWorkoutsViewModel.getState().getWorkouts().isEmpty());
    }
}
