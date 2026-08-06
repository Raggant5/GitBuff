package use_case.dashboard;

import java.time.LocalDate;
import java.util.Map;

/**
 * Interactor for loading dashboard information.
 */
public class DashboardInteractor implements DashboardInputBoundary {

    private final DashboardDataAccessInterface dashboardDataAccessObject;
    private final DashboardOutputBoundary dashboardPresenter;

    public DashboardInteractor(
            final DashboardDataAccessInterface dashboardDataAccessObject,
            final DashboardOutputBoundary dashboardPresenter
    ) {
        this.dashboardDataAccessObject = dashboardDataAccessObject;
        this.dashboardPresenter = dashboardPresenter;
    }

    @Override
    public void execute(final String userId) {
        if (userId == null || userId.isBlank()) {
            dashboardPresenter.prepareFailView(
                    "No user is currently logged in."
            );
            return;
        }

        try {
            final Map<LocalDate, Double> caloriesByDate =
                    dashboardDataAccessObject.getCaloriesByDate(userId);

            final DashboardOutputData outputData =
                    new DashboardOutputData(caloriesByDate);

            dashboardPresenter.prepareSuccessView(outputData);
        }
        catch (final RuntimeException exception) {
            dashboardPresenter.prepareFailView(
                    "Could not load dashboard calorie data."
            );
        }
    }
}