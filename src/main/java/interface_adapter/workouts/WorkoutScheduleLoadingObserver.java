package interface_adapter.workouts;

import use_case.session.UserLoggedInEvent;
import use_case.session.UserSessionObserver;

/**
 * Marks the AI-recommended workout schedule as loading immediately after login.
 */
public class WorkoutScheduleLoadingObserver implements UserSessionObserver {

    private final WorkoutsViewModel workoutsViewModel;

    /**
     * Constructs a WorkoutScheduleLoadingObserver instance.
     *
     * @param workoutsViewModel view model for the AI-recommended workout schedule
     */
    public WorkoutScheduleLoadingObserver(final WorkoutsViewModel workoutsViewModel) {
        this.workoutsViewModel = workoutsViewModel;
    }

    @Override
    public void onUserLoggedIn(final UserLoggedInEvent event) {
        final WorkoutsState workoutsState = this.workoutsViewModel.getState();
        workoutsState.setLoading(true);
        this.workoutsViewModel.firePropertyChanged();
    }
}
