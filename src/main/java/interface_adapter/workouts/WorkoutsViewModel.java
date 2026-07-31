package interface_adapter.workouts;

import interface_adapter.ViewModel;

/**
 * The View Model for the Workouts View.
 */
public class WorkoutsViewModel extends ViewModel<WorkoutsState> {

    /**
     * Constructs a WorkoutsViewModel instance.
     */
    public WorkoutsViewModel() {
        super("workouts");
        setState(new WorkoutsState());
    }
}
