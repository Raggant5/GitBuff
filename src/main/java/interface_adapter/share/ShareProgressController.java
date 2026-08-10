package interface_adapter.share;

import use_case.share.ShareProgressInputBoundary;

/**
 * Controller for Share Progress feature.
 */
public class ShareProgressController {

    private final ShareProgressInputBoundary interactor;

    public ShareProgressController(final ShareProgressInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Requests calculation and preview of share content.
     */
    public void executeFetchSharePreview() {
        this.interactor.prepareSharePreview();
    }

    /**
     * Requests sending email to specified recipient.
     *
     * @param recipientEmail email address
     */
    public void executeSendEmail(final String recipientEmail) {
        this.interactor.sendShareEmail(recipientEmail);
    }
}
