package interface_adapter.log_workout.workout;

import java.util.ArrayList;
import java.util.List;

import entity.LoggedWorkout;
import interface_adapter.MainViewManagerModel;
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
        final ViewWorkoutsState workoutsState = viewWorkoutsViewModel.getState();
        final List<LoggedWorkout> workouts = new ArrayList<>(workoutsState.getWorkouts());
        workouts.add(outputData.getWorkout());
        workoutsState.setWorkouts(workouts);
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
