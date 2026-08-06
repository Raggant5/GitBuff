package interface_adapter.dashboard;

import use_case.dashboard.DashboardOutputBoundary;
import use_case.dashboard.DashboardOutputData;

/**
 * Presenter for dashboard data.
 */
public class DashboardPresenter implements DashboardOutputBoundary {

    private final DashboardViewModel dashboardViewModel;

    public DashboardPresenter(
            final DashboardViewModel dashboardViewModel
    ) {
        this.dashboardViewModel = dashboardViewModel;
    }

    @Override
    public void prepareSuccessView(
            final DashboardOutputData outputData
    ) {
        final DashboardState state = dashboardViewModel.getState();

        state.setCaloriesByDate(
                outputData.getCaloriesByDate()
        );
        state.setErrorMessage(null);

        dashboardViewModel.setState(state);
        dashboardViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(
            final String errorMessage
    ) {
        final DashboardState state = dashboardViewModel.getState();

        state.setErrorMessage(errorMessage);

        dashboardViewModel.setState(state);
        dashboardViewModel.firePropertyChanged();
    }
}