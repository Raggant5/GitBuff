package interface_adapter.log_workout.workout;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.MainViewManagerModel;
import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import interface_adapter.log_workout.exercise.StrengthDetailsDisplayData;
import use_case.log_workout.exercise_performed.ExercisePerformedData;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutOutputBoundary;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutOutputData;

public class AddWorkoutPresenter implements AddWorkoutOutputBoundary {
    private final WorkoutEditorViewModel workoutEditorViewModel;
    private final ViewWorkoutsViewModel viewWorkoutsViewModel;
    private final MainViewManagerModel mainViewManagerModel;

    public AddWorkoutPresenter(WorkoutEditorViewModel workoutEditorViewModel,
                               ViewWorkoutsViewModel viewWorkoutsViewModel,
                               MainViewManagerModel mainViewManagerModel) {
        this.workoutEditorViewModel = workoutEditorViewModel;
        this.viewWorkoutsViewModel = viewWorkoutsViewModel;
        this.mainViewManagerModel = mainViewManagerModel;
    }

    @Override
    public void prepareSuccessView(AddWorkoutOutputData outputData) {
        final WorkoutEditorState currentState = workoutEditorViewModel.getState();
        currentState.reset();
        workoutEditorViewModel.firePropertyChanged();

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

        final ViewWorkoutsState workoutsState = viewWorkoutsViewModel.getState();
        workoutsState.addWorkout(workout);
        viewWorkoutsViewModel.setState(workoutsState);
        viewWorkoutsViewModel.firePropertyChanged();
        mainViewManagerModel.setState("view workouts");
        mainViewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        workoutEditorViewModel.getState().setErrorMessage(errorMessage);
        workoutEditorViewModel.firePropertyChanged();
    }

}
