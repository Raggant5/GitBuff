package interface_adapter.dashboard;

import use_case.dashboard.DashboardInputBoundary;

/**
 * Controller for the dashboard feature's view-triggered refresh action.
 */
public class DashboardController {

    private final DashboardInputBoundary dashboardInteractor;

    public DashboardController(final DashboardInputBoundary dashboardInteractor) {
        this.dashboardInteractor = dashboardInteractor;
    }

    /**
     * Executes the Dashboard Use Case.
     *
     * @param userId the currently logged-in user's id
     */
    public void execute(final String userId) {
        if (this.dashboardInteractor != null && userId != null && !userId.isBlank()) {
            this.dashboardInteractor.execute(userId);
        }
    }
}
