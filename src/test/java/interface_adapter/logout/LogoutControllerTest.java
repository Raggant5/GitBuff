package interface_adapter.logout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInputData;

class LogoutControllerTest {

    @Test
    void executeDelegatesToInteractorWithInputData() {
        final FakeLogoutInteractor interactor = new FakeLogoutInteractor();
        final LogoutController controller = new LogoutController(interactor);

        controller.execute("amir");

        assertEquals("amir", interactor.receivedInputData.getUsername());
    }

    private static final class FakeLogoutInteractor implements LogoutInputBoundary {
        private LogoutInputData receivedInputData;

        @Override
        public void execute(final LogoutInputData logoutInputData) {
            this.receivedInputData = logoutInputData;
        }
    }
}
