package interface_adapter.log_workout.workout;

import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutOutputBoundary;
import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutOutputData;

public class DeleteWorkoutPresenter implements DeleteWorkoutOutputBoundary {

    private final ViewWorkoutsViewModel viewWorkoutsViewModel;

    public DeleteWorkoutPresenter(ViewWorkoutsViewModel viewWorkoutsViewModel) {
        this.viewWorkoutsViewModel = viewWorkoutsViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteWorkoutOutputData deleteWorkoutOutputData) {
        viewWorkoutsViewModel.getState().setError("");
        viewWorkoutsViewModel.getState().removeWorkout(deleteWorkoutOutputData.getWorkoutId());
        viewWorkoutsViewModel.firePropertyChanged();
    }
}
