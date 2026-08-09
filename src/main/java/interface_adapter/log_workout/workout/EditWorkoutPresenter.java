package interface_adapter.log_workout.workout;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.MainViewManagerModel;
import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import interface_adapter.log_workout.exercise.StrengthDetailsDisplayData;
import use_case.log_workout.exercise_performed.ExercisePerformedData;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutOutputBoundary;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutOutputData;

public class EditWorkoutPresenter implements EditWorkoutOutputBoundary {

    private final ViewWorkoutsViewModel viewWorkoutsViewModel;
    private final WorkoutEditorViewModel workoutEditorViewModel;
    private final MainViewManagerModel mainViewManagerModel;

    public EditWorkoutPresenter(ViewWorkoutsViewModel viewWorkoutsViewModel,
                                WorkoutEditorViewModel workoutEditorViewModel,
                                MainViewManagerModel mainViewManagerModel) {
        this.viewWorkoutsViewModel = viewWorkoutsViewModel;
        this.workoutEditorViewModel = workoutEditorViewModel;
        this.mainViewManagerModel = mainViewManagerModel;
    }

    @Override
    public void prepareSuccessView(EditWorkoutOutputData outputData) {
        final List<ExercisePerformedDisplayData> exercises = new ArrayList<>();
        for (ExercisePerformedData exercise : outputData.getExercises()) {
            final StrengthDetailsDisplayData strengthDetailsDisplayData = new StrengthDetailsDisplayData(
                    exercise.getSets(), exercise.getReps(), exercise.getWeight());
            exercises.add(new ExercisePerformedDisplayData(exercise.getId(), exercise.getExerciseName(),
                    strengthDetailsDisplayData, exercise.getDurationMins(), exercise.getDistanceKm(),
                    exercise.getIsCardio()));
        }
        final LoggedWorkoutDisplayData workout = new LoggedWorkoutDisplayData(outputData.getId(),
                outputData.getDate(), exercises);

        final ViewWorkoutsState viewWorkoutsState = viewWorkoutsViewModel.getState();
        viewWorkoutsState.replaceWorkout(workout);
        viewWorkoutsViewModel.firePropertyChanged();
        workoutEditorViewModel.getState().reset();
        workoutEditorViewModel.firePropertyChanged();

        mainViewManagerModel.setState("view workouts");
        mainViewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {

        final WorkoutEditorState state = workoutEditorViewModel.getState();
        state.setErrorMessage(errorMessage);
        workoutEditorViewModel.firePropertyChanged();

    }
}
