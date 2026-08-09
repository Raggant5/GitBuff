package interface_adapter.log_workout.exercise;

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
        final ExerciseEditorState state = exerciseEditorViewModel.getState();
        state.reset();
        state.setEditingExercisePerformedId(outputData.getId());
        state.setExerciseName(outputData.getExerciseName());
        final StrengthDetailsDisplayData strengthDetailsDisplayData = new StrengthDetailsDisplayData(
                outputData.getStrengthDetailsData().getSets(), outputData.getStrengthDetailsData().getReps(),
                outputData.getStrengthDetailsData().getWeight());
        state.setSets(strengthDetailsDisplayData.getSets());
        state.setReps(strengthDetailsDisplayData.getReps());
        state.setWeight(strengthDetailsDisplayData.getWeight());
        state.setDurationMins(String.valueOf(outputData.getDurationMins()));
        state.setDistanceKm(displayDouble(outputData.getDistanceKm()));
        state.setIsCardio(outputData.getIsCardio());

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

    private String displayDouble(Double value) {
        String result = "";
        if (value != null) {
            result = String.valueOf(value);
        }
        return result;
    }
}
