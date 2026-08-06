package interface_adapter.dashboard;

import use_case.dashboard.DashboardOutputBoundary;
import use_case.dashboard.DashboardOutputData;

/**
 * Presenter for the dashboard use case.
 */
public class DashboardPresenter
        implements DashboardOutputBoundary {

    private final DashboardViewModel dashboardViewModel;

    /**
     * Constructs a DashboardPresenter.
     *
     * @param dashboardViewModel dashboard view model
     */
    public DashboardPresenter(
            final DashboardViewModel dashboardViewModel
    ) {
        this.dashboardViewModel = dashboardViewModel;
    }

    @Override
    public void prepareSuccessView(
            final DashboardOutputData outputData
    ) {
        final DashboardState state =
                this.dashboardViewModel.getState();

        state.setCaloriesByDate(
                outputData.getCaloriesByDate()
        );

        state.setMacroData(
                outputData.getMacroData()
        );

        state.setErrorMessage(null);

        this.dashboardViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(
            final String error
    ) {
        final DashboardState state =
                this.dashboardViewModel.getState();

        state.setErrorMessage(error);

        this.dashboardViewModel.firePropertyChanged();
    }
}