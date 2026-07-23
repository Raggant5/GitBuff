package interface_adapter.workouts;

import interface_adapter.ViewModel;

public class WorkoutsViewModel extends ViewModel<String> {

    public WorkoutsViewModel() {
        super("workouts");
        setState("workouts");
    }
}