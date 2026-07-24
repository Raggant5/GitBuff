package interface_adapter.workouts;

import interface_adapter.ViewModel;

public class WorkoutsViewModel extends ViewModel<WorkoutsState> {

    public WorkoutsViewModel() {
        super("workouts");
        setState(new WorkoutsState());
    }
}
