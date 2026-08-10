package interface_adapter.workouts;

import interface_adapter.recommendation.RecommendationRefreshObserver;
import interface_adapter.session.UserLoggedInEvent;
import interface_adapter.session.UserSessionObserver;

/**
 * Marks the AI-recommended workout schedule as loading immediately after login, so the
 * Workouts and Profile views can show a loading indicator while
 * {@link RecommendationRefreshObserver} generates the actual schedule in the background.
 *
 * <p>Extracted from {@code interface_adapter.login.LoginPresenter}.
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
