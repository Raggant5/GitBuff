package interface_adapter.log_workout.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import interface_adapter.MainViewManagerModel;
import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.exercise_performed.ExercisePerformedData;
import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutOutputData;

/**
 * Unit tests for the Prepare Edit Workout Presenter.
 */
class PrepareEditWorkoutPresenterTest {

    @Test
    void prepareSuccessViewPopulatesEditorStateAndSwitchesView() {
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        workoutEditorViewModel.getState().setNextTempId(5);
        final ViewWorkoutsViewModel viewWorkoutsViewModel = new ViewWorkoutsViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
        final PrepareEditWorkoutPresenter presenter = new PrepareEditWorkoutPresenter(workoutEditorViewModel,
                viewWorkoutsViewModel, mainViewManagerModel);

        final List<ExercisePerformedData> exercises = List.of(
                new ExercisePerformedData(1, "Overhead Press", new StrengthDetailsData(3, 6, 40.0),
                        0, null, false));
        final PrepareEditWorkoutOutputData outputData = new PrepareEditWorkoutOutputData(8,
                LocalDate.of(2024, 3, 3), exercises);

        presenter.prepareSuccessView(outputData);

        final WorkoutEditorState state = workoutEditorViewModel.getState();
        assertEquals(8, state.getEditingWorkoutId());
        assertEquals(LocalDate.of(2024, 3, 3), state.getDate());
        assertEquals("", state.getErrorMessage());
        assertEquals(1, state.getExercisesForWorkout().size());
        assertEquals("Overhead Press", state.getExercisesForWorkout().get(0).getExerciseName());
        assertEquals("3", state.getExercisesForWorkout().get(0).getSets());
        assertEquals("workout editor", mainViewManagerModel.getState());
    }

    @Test
    void prepareFailViewSetsErrorOnViewWorkoutsState() {
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        final ViewWorkoutsViewModel viewWorkoutsViewModel = new ViewWorkoutsViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
        final PrepareEditWorkoutPresenter presenter = new PrepareEditWorkoutPresenter(workoutEditorViewModel,
                viewWorkoutsViewModel, mainViewManagerModel);

        presenter.prepareFailView("Could not prepare workout for editing.");

        assertEquals("Could not prepare workout for editing.", viewWorkoutsViewModel.getState().getError());
        assertTrue(workoutEditorViewModel.getState().getExercisesForWorkout().isEmpty());
    }
}
