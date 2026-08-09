package interface_adapter.log_workout.workout;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.MainViewManagerModel;
import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import interface_adapter.log_workout.exercise.StrengthDetailsDisplayData;
import use_case.log_workout.exercise_performed.ExercisePerformedData;
import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutOutputBoundary;
import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutOutputData;

public class PrepareEditWorkoutPresenter implements PrepareEditWorkoutOutputBoundary {
    private final WorkoutEditorViewModel workoutEditorViewModel;
    private final ViewWorkoutsViewModel viewWorkoutsViewModel;
    private final MainViewManagerModel mainViewManagerModel;

    public PrepareEditWorkoutPresenter(WorkoutEditorViewModel workoutEditorViewModel,
                                       ViewWorkoutsViewModel viewWorkoutsViewModel,
                                       MainViewManagerModel mainViewManagerModel) {
        this.workoutEditorViewModel = workoutEditorViewModel;
        this.viewWorkoutsViewModel = viewWorkoutsViewModel;
        this.mainViewManagerModel = mainViewManagerModel;
    }

    @Override
    public void prepareSuccessView(PrepareEditWorkoutOutputData outputData) {
        final WorkoutEditorState workoutEditorState = workoutEditorViewModel.getState();
        workoutEditorState.reset();
        workoutEditorState.setEditingWorkoutId(outputData.getWorkoutId());
        workoutEditorState.setDate(outputData.getDate());
        workoutEditorState.setErrorMessage("");
        final List<ExercisePerformedDisplayData> exercises = new ArrayList<>();
        for (ExercisePerformedData exercise : outputData.getExercises()) {
            final StrengthDetailsDisplayData strengthDetailsDisplayData = new StrengthDetailsDisplayData(
                    exercise.getSets(), exercise.getReps(), exercise.getWeight());
            exercises.add(new ExercisePerformedDisplayData(exercise.getId(), exercise.getExerciseName(),
                    strengthDetailsDisplayData, exercise.getDurationMins(), exercise.getDistanceKm(),
                    exercise.getIsCardio()));
        }
        workoutEditorState.setExercisesForWorkout(exercises);

        mainViewManagerModel.setState("workout editor");
        mainViewManagerModel.firePropertyChanged();
        workoutEditorViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewWorkoutsViewModel.getState().setError(errorMessage);
        viewWorkoutsViewModel.firePropertyChanged();
    }
}
