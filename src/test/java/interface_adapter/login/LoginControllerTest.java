package interface_adapter.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

class LoginControllerTest {

    @Test
    void executeDelegatesToInteractorWithInputData() {
        final FakeLoginInteractor interactor = new FakeLoginInteractor();
        final LoginController controller = new LoginController(interactor);

        controller.execute("amir", "password");

        assertEquals("amir", interactor.receivedInputData.getUsername());
        assertEquals("password", interactor.receivedInputData.getPassword());
    }

    @Test
    void switchToSignupViewDelegatesToInteractor() {
        final FakeLoginInteractor interactor = new FakeLoginInteractor();
        final LoginController controller = new LoginController(interactor);

        controller.switchToSignupView();

        assertTrue(interactor.switchedToSignup);
    }

    private static final class FakeLoginInteractor implements LoginInputBoundary {
        private LoginInputData receivedInputData;
        private boolean switchedToSignup;

        @Override
        public void execute(final LoginInputData loginInputData) {
            this.receivedInputData = loginInputData;
        }

        @Override
        public void switchToSignupView() {
            this.switchedToSignup = true;
        }
    }
}
