package data_access;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import use_case.share.ShareEmailDataAccessInterface;

/**
 * Data Access Object for dispatching progress emails via system mail protocol.
 */
public class JavaMailDataAccessObject implements ShareEmailDataAccessInterface {

    @Override
    public boolean sendEmail(final String recipientEmail, final String subject,
                             final String bodyText, final String imagePath) {
        boolean result = false;
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
                final String encodedSubject = URLEncoder.encode(subject, StandardCharsets.UTF_8)
                        .replace("+", "%20");
                final String encodedBody = URLEncoder.encode(bodyText, StandardCharsets.UTF_8)
                        .replace("+", "%20");

                final String uriStr = String.format("mailto:%s?subject=%s&body=%s",
                        recipientEmail, encodedSubject, encodedBody);

                Desktop.getDesktop().mail(new URI(uriStr));
                result = true;
            }
        }
        catch (final Exception ex) {
            result = false;
        }
        return result;
    }
}
