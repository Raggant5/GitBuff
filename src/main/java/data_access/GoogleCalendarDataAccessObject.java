package data_access;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.AclRule;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import entity.CalendarEvent;

/**
 * Accesses calendar information through the Google Calendar API.
 */
public class GoogleCalendarDataAccessObject {
    private static final String TIME_ZONE = "America/Toronto";

    private final Calendar calendarService;

    public GoogleCalendarDataAccessObject(Calendar calendarService) {
        this.calendarService = calendarService;
    }

    public String createSecondaryCalendar(String calendarName)
            throws IOException {
        com.google.api.services.calendar.model.Calendar googleCalendar =
                new com.google.api.services.calendar.model.Calendar()
                        .setSummary(calendarName)
                        .setTimeZone(TIME_ZONE);

        return calendarService.calendars()
                .insert(googleCalendar)
                .execute()
                .getId();
    }

    public void shareCalendar(String calendarId, String userEmail)
            throws IOException {
        AclRule.Scope scope = new AclRule.Scope()
                .setType("user")
                .setValue(userEmail);

        AclRule rule = new AclRule()
                .setScope(scope)
                .setRole("reader");

        calendarService.acl()
                .insert(calendarId, rule)
                .setSendNotifications(true)
                .execute();
    }

    public CalendarEvent addAllDayEvent(
            String calendarId,
            CalendarEvent calendarEvent) throws IOException {

        LocalDate activityDate = calendarEvent.getActivityDate();

        EventDateTime start = new EventDateTime()
                .setDate(new DateTime(activityDate.toString()));

        EventDateTime end = new EventDateTime()
                .setDate(new DateTime(
                        activityDate.plusDays(1).toString()));

        Event googleEvent = new Event()
                .setSummary(calendarEvent.getTitle())
                .setDescription(calendarEvent.getDescription())
                .setStart(start)
                .setEnd(end);

        Event createdEvent = calendarService.events()
                .insert(calendarId, googleEvent)
                .execute();

        return new CalendarEvent(
                createdEvent.getId(),
                calendarEvent.getUserId(),
                calendarEvent.getTitle(),
                calendarEvent.getDescription(),
                calendarEvent.getActivityDate());
    }

    public List<CalendarEvent> loadEvents(
            String calendarId,
            String userId,
            YearMonth month) throws IOException {

        LocalDate firstDay = month.atDay(1);
        LocalDate firstDayOfNextMonth = month.plusMonths(1).atDay(1);

        Events googleEvents = calendarService.events()
                .list(calendarId)
                .setTimeMin(toGoogleDateTime(firstDay))
                .setTimeMax(toGoogleDateTime(firstDayOfNextMonth))
                .setSingleEvents(true)
                .setOrderBy("startTime")
                .execute();

        List<CalendarEvent> calendarEvents = new ArrayList<>();

        for (Event googleEvent : googleEvents.getItems()) {
            EventDateTime start = googleEvent.getStart();

            if (start != null && start.getDate() != null) {
                LocalDate activityDate = LocalDate.parse(
                        start.getDate().toStringRfc3339());

                calendarEvents.add(new CalendarEvent(
                        googleEvent.getId(),
                        userId,
                        googleEvent.getSummary(),
                        googleEvent.getDescription(),
                        activityDate));
            }
        }

        return calendarEvents;
    }

    public void removeEvent(String calendarId, String eventId)
            throws IOException {
        calendarService.events()
                .delete(calendarId, eventId)
                .execute();
    }

    private DateTime toGoogleDateTime(LocalDate date) {
        long milliseconds = date.atStartOfDay(ZoneId.of(TIME_ZONE))
                .toInstant()
                .toEpochMilli();

        return new DateTime(milliseconds);
    }
}