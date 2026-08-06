package interface_adapter.log_workout.workout;

import interface_adapter.ViewModel;

/**
 * The View Model for the Add Workout View.
 */
public class WorkoutEditorViewModel extends ViewModel<WorkoutEditorState> {

    public WorkoutEditorViewModel() {
        super("workout editor");
        setState(new WorkoutEditorState());
    }

}
