package data_access;

import java.awt.Desktop;
import java.io.File;
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
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
                final String uriStr = String.format("mailto:%s?subject=%s&body=%s",
                        recipientEmail,
                        URLEncoder.encode(subject, StandardCharsets.UTF_8),
                        URLEncoder.encode(bodyText, StandardCharsets.UTF_8));
                Desktop.getDesktop().mail(new URI(uriStr));
                return true;
            }
        }
        catch (final Exception ex) {
            return false;
        }
        return false;
    }
}
