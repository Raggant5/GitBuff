package data_access;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

/**
 * Factory for creating authorized Google Calendar API instances.
 */
public final class GoogleCalendarServiceFactory {

    private static final int PORT = 8888;

    private GoogleCalendarServiceFactory() {
        // Utility class.
    }

    /**
     * Builds and authorizes a Google Calendar service instance.
     *
     * @return authorized Calendar service.
     */
    public static Calendar create() {
        try {
            final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

            final InputStream credentialsFile = GoogleCalendarServiceFactory.class
                    .getResourceAsStream("/credentials.json");

            if (credentialsFile == null) {
                throw new IllegalStateException("credentials.json was not found.");
            }

            final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                    GsonFactory.getDefaultInstance(),
                    new InputStreamReader(credentialsFile));

            final GoogleAuthorizationCodeFlow authorizationFlow = new GoogleAuthorizationCodeFlow.Builder(
                    transport,
                    GsonFactory.getDefaultInstance(),
                    clientSecrets,
                    List.of(CalendarScopes.CALENDAR))
                    .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
                    .setAccessType("offline")
                    .build();

            final LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                    .setPort(PORT)
                    .build();

            final Credential credential = new AuthorizationCodeInstalledApp(authorizationFlow, receiver)
                    .authorize("gitbuff");

            return new Calendar.Builder(transport, GsonFactory.getDefaultInstance(), credential)
                    .setApplicationName("GitBuff")
                    .build();
        }
        catch (final Exception exception) {
            throw new IllegalStateException("Could not create Google Calendar service.", exception);
        }
    }
}