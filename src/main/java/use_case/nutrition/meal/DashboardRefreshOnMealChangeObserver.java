package use_case.nutrition.meal;

import use_case.dashboard.DashboardInputBoundary;

/**
 * Refreshes the dashboard's calorie and macro charts whenever a meal is added, edited, or
 * deleted.
 */
public class DashboardRefreshOnMealChangeObserver implements MealChangedObserver {

    private final DashboardInputBoundary dashboardInteractor;

    public DashboardRefreshOnMealChangeObserver(final DashboardInputBoundary dashboardInteractor) {
        this.dashboardInteractor = dashboardInteractor;
    }

    @Override
    public void onMealChanged(final MealChangedEvent event) {
        this.dashboardInteractor.execute(event.getUserId());
    }
}
