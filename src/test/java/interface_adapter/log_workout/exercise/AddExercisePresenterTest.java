package interface_adapter.log_workout.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import interface_adapter.log_workout.workout.WorkoutEditorState;
import interface_adapter.log_workout.workout.WorkoutEditorViewModel;
import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.exercise_performed.ExerciseValidationErrors;
import use_case.log_workout.exercise_performed.create_exercise.AddExercisePerformedOutputData;

/**
 * Unit tests for the Add Exercise Presenter.
 */
class AddExercisePresenterTest {

    @Test
    void prepareSuccessViewWithNullIdAssignsNextTempIdAndAddsExercise() {
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        final ExerciseEditorViewModel exerciseEditorViewModel = new ExerciseEditorViewModel();
        final AddExercisePresenter presenter =
                new AddExercisePresenter(workoutEditorViewModel, exerciseEditorViewModel);
        final WorkoutEditorState workoutState = workoutEditorViewModel.getState();
        workoutState.setNextTempId(-1);
        workoutState.setShowExerciseEditor(true);

        final AddExercisePerformedOutputData outputData = new AddExercisePerformedOutputData(
                null, "Bench Press", new StrengthDetailsData(3, 10, 50.0), 5.0, null, false);

        presenter.prepareSuccessView(outputData);

        assertEquals(1, workoutState.getExercisesForWorkout().size());
        final ExercisePerformedDisplayData added = workoutState.getExercisesForWorkout().get(0);
        assertEquals(-1, added.getId());
        assertEquals("Bench Press", added.getExerciseName());
        assertEquals("3", added.getSets());
        assertEquals("10", added.getReps());
        assertEquals("50.0", added.getWeight());
        assertEquals(-2, workoutState.getNextTempId());
        assertFalse(workoutState.getShowExerciseEditor());
    }

    @Test
    void prepareSuccessViewWithNonNullIdUsesGivenIdAndResetsExerciseEditor() {
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        final ExerciseEditorViewModel exerciseEditorViewModel = new ExerciseEditorViewModel();
        final AddExercisePresenter presenter =
                new AddExercisePresenter(workoutEditorViewModel, exerciseEditorViewModel);
        exerciseEditorViewModel.getState().setExerciseName("Old value");

        final AddExercisePerformedOutputData outputData = new AddExercisePerformedOutputData(
                42, "Running", new StrengthDetailsData(null, null, null), 30.0, 5.0, true);

        presenter.prepareSuccessView(outputData);

        final ExercisePerformedDisplayData added =
                workoutEditorViewModel.getState().getExercisesForWorkout().get(0);
        assertEquals(42, added.getId());
        assertEquals("Running", added.getExerciseName());
        assertEquals(30.0, added.getDurationMins());
        assertEquals(5.0, added.getDistanceKm());
        assertTrue(added.getIsCardio());
        assertEquals("", exerciseEditorViewModel.getState().getExerciseName());
    }

    @Test
    void prepareFailViewSetsErrorsOnExerciseEditorState() {
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        final ExerciseEditorViewModel exerciseEditorViewModel = new ExerciseEditorViewModel();
        final AddExercisePresenter presenter =
                new AddExercisePresenter(workoutEditorViewModel, exerciseEditorViewModel);

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
