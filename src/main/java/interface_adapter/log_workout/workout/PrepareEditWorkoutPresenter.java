package interface_adapter.log_workout.workout;

import java.util.ArrayList;

import entity.LoggedWorkout;
import interface_adapter.MainViewManagerModel;
import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutOutputBoundary;
import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutOutputData;

public class PrepareEditWorkoutPresenter implements PrepareEditWorkoutOutputBoundary {
    private final WorkoutEditorViewModel workoutEditorViewModel;
    private final MainViewManagerModel mainViewManagerModel;

    public PrepareEditWorkoutPresenter(WorkoutEditorViewModel workoutEditorViewModel,
                                       MainViewManagerModel mainViewManagerModel) {
        this.workoutEditorViewModel = workoutEditorViewModel;
        this.mainViewManagerModel = mainViewManagerModel;
    }

    @Override
    public void prepareSuccessView(PrepareEditWorkoutOutputData outputData) {
        final LoggedWorkout workout = outputData.getWorkout();
        final WorkoutEditorState workoutEditorState = workoutEditorViewModel.getState();
        workoutEditorState.reset();
        workoutEditorState.setEditingWorkout(workout);
        workoutEditorState.setDate(workout.getDate());
        workoutEditorState.setErrorMessage("");
        workoutEditorState.setExercisesForWorkout(new ArrayList<>(workout.getExercises()));

        mainViewManagerModel.setState("workout editor");
        mainViewManagerModel.firePropertyChanged();
        workoutEditorViewModel.firePropertyChanged();
    }
}
