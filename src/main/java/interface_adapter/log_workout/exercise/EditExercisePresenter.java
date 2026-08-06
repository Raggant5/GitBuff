package interface_adapter.log_workout.exercise;

import interface_adapter.log_workout.workout.WorkoutEditorState;
import interface_adapter.log_workout.workout.WorkoutEditorViewModel;
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
        final WorkoutEditorState state = workoutEditorViewModel.getState();
        state.setShowExerciseEditor(false);
        workoutEditorViewModel.firePropertyChanged();
        exerciseEditorViewModel.getState().reset();
        exerciseEditorViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        exerciseEditorViewModel.getState().setSubmitError(errorMessage);
        exerciseEditorViewModel.firePropertyChanged();

    }
}
