package interface_adapter.log_workout.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import interface_adapter.log_workout.workout.WorkoutEditorState;
import interface_adapter.log_workout.workout.WorkoutEditorViewModel;
import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.exercise_performed.ExerciseValidationErrors;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseOutputData;

/**
 * Unit tests for the Edit Exercise Presenter.
 */
class EditExercisePresenterTest {

    @Test
    void prepareSuccessViewReplacesExerciseAndHidesEditor() {
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        final ExerciseEditorViewModel exerciseEditorViewModel = new ExerciseEditorViewModel();
        final EditExercisePresenter presenter =
                new EditExercisePresenter(workoutEditorViewModel, exerciseEditorViewModel);
        final WorkoutEditorState state = workoutEditorViewModel.getState();
        state.setShowExerciseEditor(true);
        final StrengthDetailsDisplayData original = new StrengthDetailsDisplayData("3", "10", "50.0");
        state.addExercise(new ExercisePerformedDisplayData(5, "Bench Press", original, 0.0, null, false));
        exerciseEditorViewModel.getState().setExerciseName("Bench Press");

        final EditExerciseOutputData outputData = new EditExerciseOutputData(
                5, "Bench Press Updated", new StrengthDetailsData(4, 8, 60.0), 0.0, null, false);

        presenter.prepareSuccessView(outputData);

        assertEquals(1, state.getExercisesForWorkout().size());
        final ExercisePerformedDisplayData updated = state.getExercisesForWorkout().get(0);
        assertEquals("Bench Press Updated", updated.getExerciseName());
        assertEquals("4", updated.getSets());
        assertEquals("8", updated.getReps());
        assertEquals("60.0", updated.getWeight());
        assertFalse(state.getShowExerciseEditor());
        assertEquals("", exerciseEditorViewModel.getState().getExerciseName());
    }

    @Test
    void prepareFailViewSetsErrorsOnExerciseEditorState() {
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        final ExerciseEditorViewModel exerciseEditorViewModel = new ExerciseEditorViewModel();
        final EditExercisePresenter presenter =
                new EditExercisePresenter(workoutEditorViewModel, exerciseEditorViewModel);

        final ExerciseValidationErrors errors = new ExerciseValidationErrors();
        errors.setSetsError("Sets error");
        errors.setRepsError("Reps error");
        errors.setWeightError("Weight error");
        errors.setDurationError("Duration error");
        errors.setDistanceError("Distance error");
        errors.setGeneralError("General error");

        presenter.prepareFailView(errors);

        final ExerciseEditorState state = exerciseEditorViewModel.getState();
        assertEquals("Sets error", state.getSetsError());
        assertEquals("Reps error", state.getRepsError());
        assertEquals("Weight error", state.getWeightError());
        assertEquals("Duration error", state.getDurationError());
        assertEquals("Distance error", state.getDistanceError());
        assertEquals("General error", state.getSubmitError());
    }
}
