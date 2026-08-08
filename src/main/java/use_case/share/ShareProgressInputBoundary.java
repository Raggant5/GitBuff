package use_case.share;

/**
 * Boundary interface for sharing progress features.
 */
public interface ShareProgressInputBoundary {

    /**
     * Prepares and previews content to share based on active privacy settings.
     */
    void prepareSharePreview();

    /**
     * Sends the shared progress email to the target recipient.
     *
     * @param recipientEmail recipient email address
     */
    void sendShareEmail(final String recipientEmail);
}