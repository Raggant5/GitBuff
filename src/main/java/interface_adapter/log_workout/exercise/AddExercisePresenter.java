package interface_adapter.log_workout.exercise;

import interface_adapter.log_workout.workout.WorkoutEditorState;
import interface_adapter.log_workout.workout.WorkoutEditorViewModel;
import use_case.log_workout.exercise_performed.ExerciseValidationErrors;
import use_case.log_workout.exercise_performed.create_exercise.AddExercisePerformedOutputBoundary;
import use_case.log_workout.exercise_performed.create_exercise.AddExercisePerformedOutputData;

public class AddExercisePresenter implements AddExercisePerformedOutputBoundary {
    private final WorkoutEditorViewModel workoutEditorViewModel;
    private final ExerciseEditorViewModel exerciseEditorViewModel;

    public AddExercisePresenter(WorkoutEditorViewModel workoutEditorViewModel,
                                ExerciseEditorViewModel exerciseEditorViewModel) {
        this.workoutEditorViewModel = workoutEditorViewModel;
        this.exerciseEditorViewModel = exerciseEditorViewModel;
    }

    @Override
    public void prepareSuccessView(AddExercisePerformedOutputData outputData) {
        final WorkoutEditorState currentState = workoutEditorViewModel.getState();

        final Integer id;
        if (outputData.getId() == null) {
            id = currentState.getNextTempId();
            currentState.setNextTempId(id - 1);
        }
        else {
            id = outputData.getId();
        }

        final StrengthDetailsDisplayData strengthDetailsDisplayData = new StrengthDetailsDisplayData(
                outputData.getSets(), outputData.getReps(), outputData.getWeight());
        final ExercisePerformedDisplayData exercise = new ExercisePerformedDisplayData(id,
                outputData.getExerciseName(), strengthDetailsDisplayData,
                outputData.getDurationMins(), outputData.getDistanceKm(), outputData.getIsCardio());

        currentState.addExercise(exercise);
        currentState.setShowExerciseEditor(false);
        workoutEditorViewModel.firePropertyChanged();
        exerciseEditorViewModel.getState().reset();
        exerciseEditorViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(ExerciseValidationErrors errors) {
        final ExerciseEditorState currentState = exerciseEditorViewModel.getState();
        currentState.setSetsError(errors.getSetsError());
        currentState.setRepsError(errors.getRepsError());
        currentState.setWeightError(errors.getWeightError());
        currentState.setDurationError(errors.getDurationError());
        currentState.setDistanceError(errors.getDistanceError());
        currentState.setSubmitError(errors.getGeneralError());
        exerciseEditorViewModel.firePropertyChanged();
    }

}
