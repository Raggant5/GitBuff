package use_case.dashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Test;
import use_case.DataAccessException;

class DashboardInteractorTest {

    @Test
    void executeWithNullUserIdFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final CapturingPresenter presenter = new CapturingPresenter();

        new DashboardInteractor(dataAccessObject, presenter).execute(null);

        assertEquals("No user is currently logged in.", presenter.failMessage);
    }

    @Test
    void executeWithBlankUserIdFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final CapturingPresenter presenter = new CapturingPresenter();

        new DashboardInteractor(dataAccessObject, presenter).execute("   ");

        assertEquals("No user is currently logged in.", presenter.failMessage);
    }

    @Test
    void executeWithValidUserIdSucceeds() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final CapturingPresenter presenter = new CapturingPresenter();

        new DashboardInteractor(dataAccessObject, presenter).execute("amir");

        assertEquals(2000.0, presenter.successData.getCaloriesByDate().get(LocalDate.of(2026, 8, 10)));
        assertEquals(150.0, presenter.successData.getMacroData().getProtein());
        assertEquals(200.0, presenter.successData.getMacroData().getCarbs());
        assertEquals(70.0, presenter.successData.getMacroData().getFat());
    }

    @Test
    void executeWhenDataAccessThrowsFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject() {
            @Override
            public Map<LocalDate, Double> getCaloriesByDate(final String userId) {
                throw new DataAccessException("Database unavailable");
            }
        };
        final CapturingPresenter presenter = new CapturingPresenter();

        new DashboardInteractor(dataAccessObject, presenter).execute("amir");

        assertEquals("Could not load dashboard data.", presenter.failMessage);
    }

    private static class FakeDataAccessObject implements DashboardDataAccessInterface {
        @Override
        public Map<LocalDate, Double> getCaloriesByDate(final String userId) {
            return Map.of(LocalDate.of(2026, 8, 10), 2000.0);
        }

        @Override
        public MacroData getMacrosForToday(final String userId) {
            return new MacroData(150.0, 200.0, 70.0);
        }
    }

    private static final class CapturingPresenter implements DashboardOutputBoundary {
        private String failMessage;
        private DashboardOutputData successData;

        @Override
        public void prepareSuccessView(final DashboardOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(final String errorMessage) {
            this.failMessage = errorMessage;
        }
    }
}
