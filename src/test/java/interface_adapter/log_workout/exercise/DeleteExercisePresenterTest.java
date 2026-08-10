package interface_adapter.log_workout.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import interface_adapter.log_workout.workout.WorkoutEditorState;
import interface_adapter.log_workout.workout.WorkoutEditorViewModel;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseOutputData;

/**
 * Unit tests for the Delete Exercise Presenter.
 */
class DeleteExercisePresenterTest {

    @Test
    void prepareSuccessViewRemovesExerciseAndMarksItForDeletion() {
        final WorkoutEditorViewModel workoutEditorViewModel = new WorkoutEditorViewModel();
        final DeleteExercisePresenter presenter = new DeleteExercisePresenter(workoutEditorViewModel);
        final WorkoutEditorState state = workoutEditorViewModel.getState();
        final StrengthDetailsDisplayData strengthDetailsDisplayData =
                new StrengthDetailsDisplayData("3", "10", "50.0");
        state.addExercise(new ExercisePerformedDisplayData(
                5, "Bench Press", strengthDetailsDisplayData, 0.0, null, false));
        state.addExercise(new ExercisePerformedDisplayData(
                6, "Squat", strengthDetailsDisplayData, 0.0, null, false));

        presenter.prepareSuccessView(new DeleteExerciseOutputData(5));

        assertEquals(1, state.getExercisesForWorkout().size());
        assertEquals(6, state.getExercisesForWorkout().get(0).getId());
        assertTrue(state.getExercisesDeleteStage().contains(5));
    }
}
