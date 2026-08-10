package data_access;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventDataAccessInterface;

/**
 * Accesses calendar information through the Google Calendar API.
 */
public class GoogleCalendarDataAccessObject implements CalendarEventDataAccessInterface {

    private static final String TIME_ZONE = "America/Toronto";
    private static final String NOTIFICATION_EMAIL = "gitbuff686@gmail.com";
    private static final int ONE_DAY_IN_MINUTES = 24 * 60;

    private Calendar calendarService;
    private final Supplier<Calendar> calendarServiceSupplier;

    /**
     * Constructs a GoogleCalendarDataAccessObject with an initialized calendar service.
     *
     * @param calendarService Google Calendar service instance.
     */
    public GoogleCalendarDataAccessObject(final Calendar calendarService) {
        this.calendarService = calendarService;
        this.calendarServiceSupplier = () -> calendarService;
    }

    /**
     * Creates a data access object whose Google authorization is deferred until
     * the calendar is first used.
     *
     * @param calendarServiceSupplier factory for an authorized Calendar service.
     */
    public GoogleCalendarDataAccessObject(final Supplier<Calendar> calendarServiceSupplier) {
        this.calendarServiceSupplier = calendarServiceSupplier;
    }

    @Override
    public void addCalendarEvent(
            final String userId,
            final String title,
            final String description,
            final LocalDate activityDate) {

        try {
            final String calendarId = getOrCreateUserCalendar(userId);

            final EventDateTime start = new EventDateTime()
                    .setDate(new DateTime(activityDate.toString()));

            final EventDateTime end = new EventDateTime()
                    .setDate(new DateTime(activityDate.plusDays(1).toString()));

            final Event googleEvent = new Event()
                    .setSummary(title)
                    .setDescription(description)
                    .setStart(start)
                    .setEnd(end)
                    .setAttendees(List.of(
                            new EventAttendee().setEmail(NOTIFICATION_EMAIL)))
                    .setReminders(new Event.Reminders()
                            .setUseDefault(false)
                            .setOverrides(List.of(
                                    new EventReminder()
                                            .setMethod("email")
                                            .setMinutes(ONE_DAY_IN_MINUTES))));

            getCalendarService().events()
                    .insert(calendarId, googleEvent)
                    .setSendUpdates("all")
                    .execute();

        }
        catch (final IOException exception) {
            throw new IllegalStateException("Unable to add the calendar event.", exception);
        }
    }

    private String getOrCreateUserCalendar(final String userId) throws IOException {
        final String calendarDescription = "GitBuff user: " + userId;

        final CalendarList calendarList = getCalendarService().calendarList().list().execute();

        for (final CalendarListEntry calendar : calendarList.getItems()) {
            if (calendarDescription.equals(calendar.getDescription())) {
                return calendar.getId();
            }
        }

        final com.google.api.services.calendar.model.Calendar newCalendar =
                new com.google.api.services.calendar.model.Calendar()
                        .setSummary("GitBuff - " + userId)
                        .setDescription(calendarDescription)
                        .setTimeZone(TIME_ZONE);

        return getCalendarService().calendars()
                .insert(newCalendar)
                .execute()
                .getId();
    }

    @Override
    public void removeCalendarEvent(final String userId, final String eventId) {
        try {
            final String calendarId = getOrCreateUserCalendar(userId);

            getCalendarService().events()
                    .delete(calendarId, eventId)
                    .execute();
        }
        catch (final IOException exception) {
            throw new IllegalStateException("Unable to remove the calendar event.", exception);
        }
    }

    @Override
    public List<CalendarEvent> getUserEvents(final String userId) {
        try {
            final String calendarId = getOrCreateUserCalendar(userId);
            final List<CalendarEvent> userEvents = new ArrayList<>();

            String pageToken = null;

            do {
                final Events googleEvents = getCalendarService().events()
                        .list(calendarId)
                        .setSingleEvents(true)
                        .setOrderBy("startTime")
                        .setPageToken(pageToken)
                        .execute();

                for (final Event googleEvent : googleEvents.getItems()) {
                    if (!"cancelled".equals(googleEvent.getStatus()) && googleEvent.getStart() != null) {
                        final LocalDate activityDate = getActivityDate(googleEvent);

                        final CalendarEvent calendarEvent = new CalendarEvent(
                                googleEvent.getId(),
                                userId,
                                googleEvent.getSummary(),
                                googleEvent.getDescription(),
                                activityDate);

                        userEvents.add(calendarEvent);
                    }
                }

                pageToken = googleEvents.getNextPageToken();
            }
            while (pageToken != null);

            return userEvents;
        }
        catch (final IOException exception) {
            throw new IllegalStateException("Unable to load the user's calendar events.", exception);
        }
    }

    private LocalDate getActivityDate(final Event googleEvent) {
        final DateTime allDayDate = googleEvent.getStart().getDate();

        if (allDayDate != null) {
            return LocalDate.parse(allDayDate.toStringRfc3339());
        }

        final DateTime dateTime = googleEvent.getStart().getDateTime();

        return Instant.ofEpochMilli(dateTime.getValue())
                .atZone(ZoneId.of(TIME_ZONE))
                .toLocalDate();
    }

    private Calendar getCalendarService() {
        if (this.calendarService == null) {
            this.calendarService = this.calendarServiceSupplier.get();
        }
        return this.calendarService;
    }
}
