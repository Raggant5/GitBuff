package interface_adapter.log_workout.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import interface_adapter.log_workout.workout.WorkoutEditorViewModel;
import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.exercise_performed.prepare_edit_exercise.PrepareEditExerciseOutputData;

/**
 * Unit tests for the Prepare Edit Exercise Presenter.
 */
class PrepareEditExercisePresenterTest {

    @Test
    void prepareSuccessViewPopulatesExerciseEditorStateAndShowsEditor() {
        final ExerciseEditorViewModel exerciseEditorViewModel = new ExerciseEditorViewModel();
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        final PrepareEditExercisePresenter presenter =
                new PrepareEditExercisePresenter(exerciseEditorViewModel, workoutEditorViewModel);
        exerciseEditorViewModel.getState().setExerciseName("Stale value");

        final PrepareEditExerciseOutputData outputData = new PrepareEditExerciseOutputData(
                8, "Deadlift", new StrengthDetailsData(5, 5, 100.0), 0.0, 3.5, false);

        presenter.prepareSuccessView(outputData);

        final ExerciseEditorState state = exerciseEditorViewModel.getState();
        assertEquals(8, state.getEditingExercisePerformedId());
        assertEquals("Deadlift", state.getExerciseName());
        assertEquals("5", state.getSets());
        assertEquals("5", state.getReps());
        assertEquals("100.0", state.getWeight());
        assertEquals("0.0", state.getDurationMins());
        assertEquals("3.5", state.getDistanceKm());
        assertEquals(false, state.getIsCardio());
        assertTrue(workoutEditorViewModel.getState().getShowExerciseEditor());
    }

    @Test
    void prepareSuccessViewWithNullDistanceProducesEmptyDistanceDisplay() {
        final ExerciseEditorViewModel exerciseEditorViewModel = new ExerciseEditorViewModel();
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        final PrepareEditExercisePresenter presenter =
                new PrepareEditExercisePresenter(exerciseEditorViewModel, workoutEditorViewModel);

        final PrepareEditExerciseOutputData outputData = new PrepareEditExerciseOutputData(
                9, "Running", new StrengthDetailsData(null, null, null), 20.0, null, true);

        presenter.prepareSuccessView(outputData);

        final ExerciseEditorState state = exerciseEditorViewModel.getState();
        assertEquals("", state.getDistanceKm());
        assertEquals(true, state.getIsCardio());
    }

    @Test
    void prepareSuccessViewResetsExerciseEditorStateFirst() {
        final ExerciseEditorViewModel exerciseEditorViewModel = new ExerciseEditorViewModel();
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        final PrepareEditExercisePresenter presenter =
                new PrepareEditExercisePresenter(exerciseEditorViewModel, workoutEditorViewModel);
        exerciseEditorViewModel.getState().setSetsError("Old error");

        final PrepareEditExerciseOutputData outputData = new PrepareEditExerciseOutputData(
                1, "Plank", new StrengthDetailsData(null, null, null), 1.0, null, false);

        presenter.prepareSuccessView(outputData);

        assertEquals("", exerciseEditorViewModel.getState().getSetsError());
    }
}
