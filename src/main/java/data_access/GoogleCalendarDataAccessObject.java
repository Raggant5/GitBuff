package data_access;

import java.io.IOException;
import java.time.LocalDate;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.calendar.model.Events;

import entity.CalendarEvent;
import use_case.calendar.CalendarEventDataAccessInterface;

/**
 * Accesses calendar information through the Google Calendar API.
 */
public class GoogleCalendarDataAccessObject implements CalendarEventDataAccessInterface {
    private static final String TIME_ZONE = "America/Toronto";

    private final Calendar calendarService;

    public GoogleCalendarDataAccessObject(Calendar calendarService) {
        this.calendarService = calendarService;
    }

    @Override
    public void addCalendarEvent(
            String userId,
            String title,
            String description,
            LocalDate activityDate) {

        try {
            String calendarId = getOrCreateUserCalendar(userId);

            EventDateTime start = new EventDateTime()
                    .setDate(new DateTime(activityDate.toString()));

            EventDateTime end = new EventDateTime()
                    .setDate(new DateTime(
                            activityDate.plusDays(1).toString()));

            Event googleEvent = new Event()
                    .setSummary(title)
                    .setDescription(description)
                    .setStart(start)
                    .setEnd(end);

            Event createdEvent = calendarService.events()
                    .insert(calendarId, googleEvent)
                    .execute();

        }
        catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to add the calendar event.", exception);
        }
    }

    private String getOrCreateUserCalendar(String userId)
            throws IOException {

        String calendarDescription = "GitBuff user: " + userId;

        CalendarList calendarList =
                calendarService.calendarList().list().execute();

        for (CalendarListEntry calendar : calendarList.getItems()) {
            if (calendarDescription.equals(calendar.getDescription())) {
                return calendar.getId();
            }
        }

        com.google.api.services.calendar.model.Calendar newCalendar =
                new com.google.api.services.calendar.model.Calendar()
                        .setSummary("GitBuff - " + userId)
                        .setDescription(calendarDescription)
                        .setTimeZone(TIME_ZONE);

        return calendarService.calendars()
                .insert(newCalendar)
                .execute()
                .getId();
    }

    @Override
    public void removeCalendarEvent(String userId, String eventId) {
        try {
            String calendarId = getOrCreateUserCalendar(userId);

            calendarService.events()
                    .delete(calendarId, eventId)
                    .execute();
        }
        catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to remove the calendar event.", exception);
        }
    }

    @Override
    public List<CalendarEvent> getUserEvents(String userId) {
        try {
            String calendarId = getOrCreateUserCalendar(userId);
            List<CalendarEvent> userEvents = new ArrayList<>();

            String pageToken = null;

            do {
                Events googleEvents = calendarService.events()
                        .list(calendarId)
                        .setSingleEvents(true)
                        .setOrderBy("startTime")
                        .setPageToken(pageToken)
                        .execute();

                for (Event googleEvent : googleEvents.getItems()) {
                    if (!"cancelled".equals(googleEvent.getStatus())
                            && googleEvent.getStart() != null) {

                        LocalDate activityDate =
                                getActivityDate(googleEvent);

                        CalendarEvent calendarEvent =
                                new CalendarEvent(
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
        catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to load the user's calendar events.",
                    exception);
        }
    }

    private LocalDate getActivityDate(Event googleEvent) {
        DateTime allDayDate = googleEvent.getStart().getDate();

        if (allDayDate != null) {
            return LocalDate.parse(allDayDate.toStringRfc3339());
        }

        DateTime dateTime = googleEvent.getStart().getDateTime();

        return Instant.ofEpochMilli(dateTime.getValue())
                .atZone(ZoneId.of(TIME_ZONE))
                .toLocalDate();
    }
}