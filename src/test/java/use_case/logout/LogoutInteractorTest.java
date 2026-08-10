package use_case.logout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class LogoutInteractorTest {

    @Test
    void executeClearsCurrentUserAndPresentsSuccess() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        dataAccessObject.currentUsername = "amir";
        final CapturingPresenter presenter = new CapturingPresenter();

        new LogoutInteractor(dataAccessObject, presenter).execute(new LogoutInputData("amir"));

        assertNull(dataAccessObject.currentUsername);
        assertEquals("amir", presenter.successData.getUsername());
        assertFalse(presenter.successData.isUseCaseFailed());
    }

    private static final class FakeDataAccessObject implements LogoutUserDataAccessInterface {
        private String currentUsername;

        @Override
        public String getCurrentUsername() {
            return currentUsername;
        }

        @Override
        public void setCurrentUsername(final String username) {
            this.currentUsername = username;
        }
    }

    private static final class CapturingPresenter implements LogoutOutputBoundary {
        private LogoutOutputData successData;

        @Override
        public void prepareSuccessView(final LogoutOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(final String errorMessage) {
            throw new AssertionError("Expected success view, got failure: " + errorMessage);
        }
    }
}
