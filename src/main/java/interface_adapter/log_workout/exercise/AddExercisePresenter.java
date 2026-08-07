package interface_adapter.log_workout.exercise;

import interface_adapter.log_workout.workout.WorkoutEditorState;
import interface_adapter.log_workout.workout.WorkoutEditorViewModel;
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
        currentState.addExercise(outputData.getExercisePerformed());
        currentState.setShowExerciseEditor(false);
        workoutEditorViewModel.firePropertyChanged();
        exerciseEditorViewModel.getState().reset();
        exerciseEditorViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final ExerciseEditorState currentState = exerciseEditorViewModel.getState();
        currentState.setSubmitError(errorMessage);
        exerciseEditorViewModel.firePropertyChanged();
    }

}
