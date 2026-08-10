package interface_adapter.signup;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

class SignupControllerTest {

    @Test
    void executeDelegatesToInteractorWithInputData() {
        final FakeSignupInteractor interactor = new FakeSignupInteractor();
        final SignupController controller = new SignupController(interactor);

        controller.execute("amir", "password", "password");

        assertEquals("amir", interactor.receivedInputData.getUsername());
        assertEquals("password", interactor.receivedInputData.getPassword());
        assertEquals("password", interactor.receivedInputData.getRepeatPassword());
    }

    @Test
    void switchToLoginViewWithCredentialsDelegatesToInteractor() {
        final FakeSignupInteractor interactor = new FakeSignupInteractor();
        final SignupController controller = new SignupController(interactor);

        controller.switchToLoginView("amir", "password");

        assertEquals("amir", interactor.switchedUsername);
        assertEquals("password", interactor.switchedPassword);
    }

    @Test
    void switchToLoginViewWithoutArgsDelegatesWithEmptyCredentials() {
        final FakeSignupInteractor interactor = new FakeSignupInteractor();
        final SignupController controller = new SignupController(interactor);

        controller.switchToLoginView();

        assertEquals("", interactor.switchedUsername);
        assertEquals("", interactor.switchedPassword);
    }

    private static final class FakeSignupInteractor implements SignupInputBoundary {
        private SignupInputData receivedInputData;
        private String switchedUsername;
        private String switchedPassword;

        @Override
        public void execute(final SignupInputData signupInputData) {
            this.receivedInputData = signupInputData;
        }

        @Override
        public void switchToLoginView(final String username, final String password) {
            this.switchedUsername = username;
            this.switchedPassword = password;
        }
    }
}
