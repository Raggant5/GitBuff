package interface_adapter.dashboard;

import interface_adapter.session.UserLoggedInEvent;
import interface_adapter.session.UserSessionObserver;
import use_case.dashboard.DashboardInputBoundary;

/**
 * Refreshes the dashboard's calorie and macro charts for the newly logged-in user.
 *
 * <p>Extracted from {@code interface_adapter.login.LoginPresenter}.
 */
public class DashboardRefreshObserver implements UserSessionObserver {

    private final DashboardInputBoundary dashboardInteractor;

    /**
     * Constructs a DashboardRefreshObserver instance.
     *
     * @param dashboardInteractor interactor for loading dashboard data
     */
    public DashboardRefreshObserver(final DashboardInputBoundary dashboardInteractor) {
        this.dashboardInteractor = dashboardInteractor;
    }

    @Override
    public void onUserLoggedIn(final UserLoggedInEvent event) {
        this.dashboardInteractor.execute(event.data().getUsername());
    }
}
