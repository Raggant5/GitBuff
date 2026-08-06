package interface_adapter.log_workout.exercise;

import entity.ExercisePerformed;
import interface_adapter.log_workout.workout.WorkoutEditorState;
import interface_adapter.log_workout.workout.WorkoutEditorViewModel;
import use_case.log_workout.exercise_performed.prepare_edit_exercise.PrepareEditExerciseOutputBoundary;
import use_case.log_workout.exercise_performed.prepare_edit_exercise.PrepareEditExerciseOutputData;

public class PrepareEditExercisePresenter implements PrepareEditExerciseOutputBoundary {
    private final ExerciseEditorViewModel exerciseEditorViewModel;
    private final WorkoutEditorViewModel workoutEditorViewModel;

    public PrepareEditExercisePresenter(ExerciseEditorViewModel exerciseEditorViewModel,
                                        WorkoutEditorViewModel workoutEditorViewModel) {
        this.exerciseEditorViewModel = exerciseEditorViewModel;
        this.workoutEditorViewModel = workoutEditorViewModel;
    }

    @Override
    public void prepareSuccessView(PrepareEditExerciseOutputData outputData) {
        final ExercisePerformed exercise = outputData.getExercisePerformed();

        final ExerciseEditorState state = exerciseEditorViewModel.getState();
        state.reset();
        state.setEditingExercise(exercise);
        state.setExerciseName(exercise.getExerciseName());
        state.setSets(displayInt(exercise.getSets()));
        state.setReps(displayInt(exercise.getReps()));
        state.setWeight(displayDouble(exercise.getWeight()));
        state.setDurationMins(String.valueOf(exercise.getDurationMins()));
        state.setDistanceKm(displayDouble(exercise.getDistanceKm()));
        state.setIsCardio(exercise.getIsCardio());

        final WorkoutEditorState workoutState = workoutEditorViewModel.getState();
        workoutState.setShowExerciseEditor(true);
        exerciseEditorViewModel.firePropertyChanged();
        workoutEditorViewModel.firePropertyChanged();
    }

    @Override
    public void switchToAddExerciseEditor() {
        final WorkoutEditorState state = workoutEditorViewModel.getState();
        state.setShowExerciseEditor(true);
        workoutEditorViewModel.firePropertyChanged();
    }

    private String displayInt(Integer value) {
        String result = "";
        if (value != null) {
            result = String.valueOf(value);
        }
        return result;
    }

    private String displayDouble(Double value) {
        String result = "";
        if (value != null) {
            result = String.valueOf(value);
        }
        return result;
    }
}
