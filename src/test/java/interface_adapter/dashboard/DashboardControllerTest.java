package interface_adapter.dashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.dashboard.DashboardInputBoundary;

class DashboardControllerTest {

    @Test
    void executeWithValidUserIdDelegatesToInteractor() {
        final FakeDashboardInteractor interactor = new FakeDashboardInteractor();
        final DashboardController controller = new DashboardController(interactor);

        controller.execute("aahir");

        assertTrue(interactor.executeCalled);
        assertEquals("aahir", interactor.receivedUserId);
    }

    @Test
    void executeWithBlankUserIdDoesNotDelegate() {
        final FakeDashboardInteractor interactor = new FakeDashboardInteractor();
        final DashboardController controller = new DashboardController(interactor);

        controller.execute("   ");

        assertFalse(interactor.executeCalled);
    }

    @Test
    void executeWithNullUserIdDoesNotDelegate() {
        final FakeDashboardInteractor interactor = new FakeDashboardInteractor();
        final DashboardController controller = new DashboardController(interactor);

        controller.execute(null);

        assertFalse(interactor.executeCalled);
    }

    private static final class FakeDashboardInteractor implements DashboardInputBoundary {
        private boolean executeCalled;
        private String receivedUserId;

        @Override
        public void execute(final String userId) {
            this.executeCalled = true;
            this.receivedUserId = userId;
        }
    }
}
