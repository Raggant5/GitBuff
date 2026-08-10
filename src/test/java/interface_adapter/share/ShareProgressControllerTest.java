package interface_adapter.share;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import use_case.share.ShareProgressInputBoundary;

class ShareProgressControllerTest {

    @Test
    void executeFetchSharePreviewDelegatesToInteractor() {
        final FakeShareProgressInteractor interactor = new FakeShareProgressInteractor();
        final ShareProgressController controller = new ShareProgressController(interactor);

        controller.executeFetchSharePreview();

        assertTrue(interactor.previewRequested);
    }

    @Test
    void executeSendEmailDelegatesToInteractorWithRecipient() {
        final FakeShareProgressInteractor interactor = new FakeShareProgressInteractor();
        final ShareProgressController controller = new ShareProgressController(interactor);

        controller.executeSendEmail("friend@example.com");

        assertTrue(interactor.emailRequested);
        assertTrue(interactor.receivedRecipient.equals("friend@example.com"));
    }

    private static final class FakeShareProgressInteractor implements ShareProgressInputBoundary {
        private boolean previewRequested;
        private boolean emailRequested;
        private String receivedRecipient;

        @Override
        public void prepareSharePreview() {
            this.previewRequested = true;
        }

        @Override
        public void sendShareEmail(final String recipientEmail) {
            this.emailRequested = true;
            this.receivedRecipient = recipientEmail;
        }
    }
}
