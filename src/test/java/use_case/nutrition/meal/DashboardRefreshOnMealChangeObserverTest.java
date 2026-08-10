package use_case.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import use_case.dashboard.DashboardInputBoundary;

class DashboardRefreshOnMealChangeObserverTest {

    @Test
    void onMealChangedRefreshesDashboardForUserId() {
        final String[] receivedUserId = {null};
        final DashboardInputBoundary dashboardInteractor = userId -> receivedUserId[0] = userId;
        final DashboardRefreshOnMealChangeObserver observer =
                new DashboardRefreshOnMealChangeObserver(dashboardInteractor);

        observer.onMealChanged(new MealChangedEvent("aahir", 1, "Breakfast", LocalDate.of(2026, 1, 1),
                MealChangeType.ADDED));

        assertEquals("aahir", receivedUserId[0]);
    }
}
