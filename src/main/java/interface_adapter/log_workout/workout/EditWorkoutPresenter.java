package interface_adapter.log_workout.workout;

import java.util.List;
import java.util.Objects;

import entity.LoggedWorkout;
import interface_adapter.MainViewManagerModel;
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
        final ViewWorkoutsState viewWorkoutsState = viewWorkoutsViewModel.getState();
        final List<LoggedWorkout> workouts = viewWorkoutsState.getWorkouts();
        for (int i = 0; i < workouts.size(); i++) {
            if (Objects.equals(workouts.get(i).getId(), outputData.getWorkout().getId())) {
                workouts.set(i, outputData.getWorkout());
                break;
            }
        }

        viewWorkoutsState.setWorkouts(workouts);
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
