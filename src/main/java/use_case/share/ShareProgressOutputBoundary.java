package use_case.share;

/**
 * Presenter boundary interface for share progress notifications.
 */
public interface ShareProgressOutputBoundary {

    /**
     * Prepares view with calculated preview content.
     *
     * @param outputData output data containing formatted preview text and image path
     */
    void preparePreviewView(ShareProgressOutputData outputData);

    /**
     * Prepares success view after sending email.
     *
     * @param message success message
     */
    void prepareSendSuccessView(String message);

    /**
     * Prepares failure view.
     *
     * @param errorMessage error message
     */
    void prepareFailView(String errorMessage);
}
