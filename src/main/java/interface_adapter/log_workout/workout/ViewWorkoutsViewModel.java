package interface_adapter.log_workout.workout;

import interface_adapter.ViewModel;

public class ViewWorkoutsViewModel extends ViewModel<ViewWorkoutsState> {
    public ViewWorkoutsViewModel() {
        super("view workouts");
        setState(new ViewWorkoutsState());
    }
}
