package interface_adapter.log_workout.exercise;

import interface_adapter.log_workout.workout.WorkoutEditorState;
import interface_adapter.log_workout.workout.WorkoutEditorViewModel;
import use_case.log_workout.exercise_performed.ExerciseValidationErrors;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseOutputBoundary;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseOutputData;

public class EditExercisePresenter implements EditExerciseOutputBoundary {

    private final WorkoutEditorViewModel workoutEditorViewModel;
    private final ExerciseEditorViewModel exerciseEditorViewModel;

    public EditExercisePresenter(WorkoutEditorViewModel workoutEditorViewModel,
                                 ExerciseEditorViewModel exerciseEditorViewModel) {
        this.workoutEditorViewModel = workoutEditorViewModel;
        this.exerciseEditorViewModel = exerciseEditorViewModel;
    }

    @Override
    public void prepareSuccessView(EditExerciseOutputData outputData) {
        final StrengthDetailsDisplayData strengthDetailsDisplayData = new StrengthDetailsDisplayData(
                outputData.getSets(), outputData.getReps(), outputData.getWeight());
        final ExercisePerformedDisplayData updated = new ExercisePerformedDisplayData(outputData.getId(),
                outputData.getExerciseName(), strengthDetailsDisplayData,
                outputData.getDurationMins(), outputData.getDistanceKm(), outputData.getIsCardio());

        final WorkoutEditorState state = workoutEditorViewModel.getState();
        state.replaceExercise(updated);
        state.setShowExerciseEditor(false);
        workoutEditorViewModel.firePropertyChanged();
        exerciseEditorViewModel.getState().reset();
        exerciseEditorViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(ExerciseValidationErrors errors) {
        final ExerciseEditorState state = exerciseEditorViewModel.getState();
        state.setSetsError(errors.getSetsError());
        state.setRepsError(errors.getRepsError());
        state.setWeightError(errors.getWeightError());
        state.setDurationError(errors.getDurationError());
        state.setDistanceError(errors.getDistanceError());
        state.setSubmitError(errors.getGeneralError());
        exerciseEditorViewModel.firePropertyChanged();
    }
}
