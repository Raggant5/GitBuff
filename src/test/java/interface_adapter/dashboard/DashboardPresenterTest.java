package interface_adapter.dashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Test;
import use_case.dashboard.DashboardOutputData;
import use_case.dashboard.MacroData;

class DashboardPresenterTest {

    @Test
    void prepareSuccessViewPopulatesDashboardState() {
        final DashboardViewModel dashboardViewModel = new DashboardViewModel();
        final DashboardPresenter presenter = new DashboardPresenter(dashboardViewModel);
        final Map<LocalDate, Double> calories = Map.of(LocalDate.of(2026, 8, 10), 2000.0);
        final MacroData macroData = new MacroData(150.0, 200.0, 70.0);

        presenter.prepareSuccessView(new DashboardOutputData(calories, macroData));

        assertEquals(calories, dashboardViewModel.getState().getCaloriesByDate());
        assertEquals(macroData, dashboardViewModel.getState().getMacroData());
        assertNull(dashboardViewModel.getState().getErrorMessage());
    }

    @Test
    void prepareFailViewSetsErrorMessage() {
        final DashboardViewModel dashboardViewModel = new DashboardViewModel();
        final DashboardPresenter presenter = new DashboardPresenter(dashboardViewModel);

        presenter.prepareFailView("Could not load dashboard data.");

        assertEquals("Could not load dashboard data.", dashboardViewModel.getState().getErrorMessage());
    }
}
