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
        final DashboardState newState =
                new DashboardState();

        newState.setCaloriesByDate(
                outputData.getCaloriesByDate()
        );

        newState.setMacroData(
                outputData.getMacroData()
        );

        newState.setErrorMessage(null);

        this.dashboardViewModel.setState(newState);
        this.dashboardViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(
            final String errorMessage
    ) {
        final DashboardState newState =
                new DashboardState();

        newState.setErrorMessage(errorMessage);

        this.dashboardViewModel.setState(newState);
        this.dashboardViewModel.firePropertyChanged();
    }
}