package interface_adapter.log_workout.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import interface_adapter.MainViewManagerModel;
import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.exercise_performed.ExercisePerformedData;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutOutputData;

/**
 * Unit tests for the Edit Workout Presenter.
 */
class EditWorkoutPresenterTest {

    @Test
    void prepareSuccessViewReplacesWorkoutResetsEditorAndSwitchesView() {
        final ViewWorkoutsViewModel viewWorkoutsViewModel = new ViewWorkoutsViewModel();
        viewWorkoutsViewModel.getState().addWorkout(new LoggedWorkoutDisplayData(3, LocalDate.of(2023, 5, 1),
                List.of()));
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        workoutEditorViewModel.getState().setErrorMessage("stale error");
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
        final EditWorkoutPresenter presenter = new EditWorkoutPresenter(viewWorkoutsViewModel,
                workoutEditorViewModel, mainViewManagerModel);

        final List<ExercisePerformedData> exercises = List.of(
                new ExercisePerformedData(1, "Squat", new StrengthDetailsData(4, 8, 100.0), 0, null, false));
        final EditWorkoutOutputData outputData = new EditWorkoutOutputData(3, LocalDate.of(2024, 6, 1), exercises);

        presenter.prepareSuccessView(outputData);

        final ViewWorkoutsState viewWorkoutsState = viewWorkoutsViewModel.getState();
        assertEquals(1, viewWorkoutsState.getWorkouts().size());
        assertEquals(LocalDate.of(2024, 6, 1), viewWorkoutsState.getWorkouts().get(0).getDate());
        assertEquals("", workoutEditorViewModel.getState().getErrorMessage());
        assertEquals("view workouts", mainViewManagerModel.getState());
    }

    @Test
    void prepareFailViewSetsErrorMessageOnEditorState() {
        final ViewWorkoutsViewModel viewWorkoutsViewModel = new ViewWorkoutsViewModel();
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
        final EditWorkoutPresenter presenter = new EditWorkoutPresenter(viewWorkoutsViewModel,
                workoutEditorViewModel, mainViewManagerModel);

        presenter.prepareFailView("Could not edit workout.");

        assertEquals("Could not edit workout.", workoutEditorViewModel.getState().getErrorMessage());
        assertTrue(viewWorkoutsViewModel.getState().getWorkouts().isEmpty());
    }
}
