package use_case.session;

import use_case.dashboard.DashboardInputBoundary;

/**
 * Refreshes the dashboard's calorie and macro charts for the newly logged-in user.
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
        this.dashboardInteractor.execute(event.getData().getUsername());
    }
}
