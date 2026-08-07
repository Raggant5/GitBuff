package use_case.share;

/**
 * Data access interface for sending email notifications.
 */
public interface ShareEmailDataAccessInterface {

    /**
     * Sends an email with subject, text content, and optional image attachment.
     *
     * @param recipientEmail target recipient email
     * @param subject email subject
     * @param bodyText formatted message body
     * @param imagePath optional image file path (nullable)
     * @return true if sent successfully, false otherwise
     */
    boolean sendEmail(final String recipientEmail, final String subject, final String bodyText, final String imagePath);
}
