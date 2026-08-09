package use_case.dashboard;

import java.time.LocalDate;
import java.util.Map;

import use_case.DataAccessException;

/**
 * Interactor for loading dashboard information.
 */
public class DashboardInteractor implements DashboardInputBoundary {

    private final DashboardDataAccessInterface dashboardDataAccessObject;
    private final DashboardOutputBoundary dashboardPresenter;

    /**
     * Constructs a DashboardInteractor.
     *
     * @param dashboardDataAccessObject dashboard data access object
     * @param dashboardPresenter dashboard output boundary
     */
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
            this.dashboardPresenter.prepareFailView(
                    "No user is currently logged in.");
        }
        else {
            try {
                final Map<LocalDate, Double> caloriesByDate =
                        this.dashboardDataAccessObject.getCaloriesByDate(
                                userId
                        );

                final MacroData macroData =
                        this.dashboardDataAccessObject.getMacrosForToday(
                                userId
                        );

                final DashboardOutputData outputData =
                        new DashboardOutputData(
                                caloriesByDate,
                                macroData
                        );

                this.dashboardPresenter.prepareSuccessView(
                        outputData
                );
            }
            catch (final DataAccessException exception) {
                this.dashboardPresenter.prepareFailView(
                        "Could not load dashboard data."
                );
            }
        }
    }
}