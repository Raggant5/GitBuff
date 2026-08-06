package interface_adapter.log_workout.exercise;

import interface_adapter.log_workout.workout.WorkoutEditorViewModel;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseOutputBoundary;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseOutputData;

public class DeleteExercisePresenter implements DeleteExerciseOutputBoundary {

    private final WorkoutEditorViewModel workoutEditorViewModel;

    public DeleteExercisePresenter(WorkoutEditorViewModel workoutEditorViewModel) {
        this.workoutEditorViewModel = workoutEditorViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteExerciseOutputData deleteExerciseOutputData) {
        workoutEditorViewModel.getState().removeExercise(deleteExerciseOutputData.getExercisePerformed());
        workoutEditorViewModel.getState().addExerciseToBeDeleted(deleteExerciseOutputData.getExercisePerformed());
        workoutEditorViewModel.firePropertyChanged();
    }
}
